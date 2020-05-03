package com.leyou.user.service.impl;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.domain.User;
import com.leyou.user.mapper.IUserMapper;
import com.leyou.user.service.IUserService;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;
    static final String KEY_PREFIX = "user:code:phone:";
    static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public Boolean checkUser(String data, Integer type) {

        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }


        return this.userMapper.selectCount(record)==0;
    }

    /**
     *  发送短信
     * @param phone
     * @return
     */
    @Override
    public void sendVerifyCode(String phone) {
        if(StringUtils.isBlank(phone)){
            return ;
        }
        //生成验证码
        String code = NumberUtils.generateCode(6);
        //发送消息给rabbitMq
        Map<String,String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        this.amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE","sms.verify.code",msg);

        //把验证码保存到redis
        this.redisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);

    }

    /**
     * 注册校验
     * @param user
     * @param code
     * @return
     */
    @Override
    public Boolean register(User user, String code) {
        // 校验短信验证码
        String cacheCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.equals(code, cacheCode)) {
            return false;
        }

        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        // 对密码加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());
        // 添加到数据库
        boolean b = this.userMapper.insertSelective(user) == 1;

        if(b){
            // 注册成功，删除redis中的记录
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }
        return b;
    }

    /**
     * 查询用户是否存在
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        //先根据用户名查询是否存在
        User resultUser = this.userMapper.selectOne(record);
        //不存在
        if(resultUser == null){
            return null;
        }
        //存在的话 获取这个账号的盐值
        String salt = resultUser.getSalt();
        //将用户输入的密码加盐 然后与数据库中此账号实际密码比对
         if(!resultUser.getPassword().equals(CodecUtils.md5Hex(password, salt))){
            return null;
         }
        return resultUser;
    }


}
