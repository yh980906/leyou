package com.leyou.user.controller;


import com.leyou.user.domain.User;
import com.leyou.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {


    @Autowired
    private IUserService userService;

    /**
     *实现用户数据的校验，主要包括对：手机号、用户名的唯一性校验。
     * 1是手机号
     * 2是用户名
     * @param data
     * @param type
     * @return
     */
    @RequestMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data, @PathVariable("type")Integer type){

        Boolean bool= userService.checkUser(data,type);
        if(bool == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bool);
    }

    /**
     * 生成短信验证码并发送
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone){
        this.userService.sendVerifyCode(phone);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 注册校验
     * valid注解是校验注解
     * @param user
     * @param code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {
        Boolean boo = this.userService.register(user, code);
        if (boo == null || !boo) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 返回的格式如下： 因为不需要返回密码 所以在密码字段上加入了@JsonIgnore注解
     *
     * {
     *     "id": 6572312,
     *     "username":"test",
     *     "phone":"13688886666",
     *     "created": 1342432424
     * }
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password){
        User user = this.userService.queryUser(username,password);
        if(user == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

}
