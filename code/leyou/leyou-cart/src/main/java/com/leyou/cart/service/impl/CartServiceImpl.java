package com.leyou.cart.service.impl;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.domain.Cart;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.service.ICartService;
import com.leyou.common.domain.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.domain.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "leyou:cart:uid:";

    @Autowired
    private GoodsClient goodsClient;
    /**
     * 加入购物车
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //获取登录的用户
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //redis的hash的key
        String key = KEY_PREFIX + userInfo.getId();
        //获取redis的hash操作对象
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);

        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean hashBoolean = hashOperations.hasKey(skuId.toString());
        if(hashBoolean){
            //存在 从缓存中获取购物车数据 只需要更改数量
            String json = hashOperations.get(skuId.toString()).toString();
            cart = JsonUtils.parse(json, Cart.class);
            //修改购物车数量
            cart.setNum(num+cart.getNum());

        }else {
            //不存在，新增购物车数据
            cart.setUserId(userInfo.getId());
            //通过skuId查询Sku
            Sku sku = goodsClient.querySkuById(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        hashOperations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<Cart> queryCarts() {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        if(!this.redisTemplate.hasKey(key)){
            // 不存在，直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        // 判断是否有数据
        if(CollectionUtils.isEmpty(carts)){
            return null;
        }
        // 查询购物车数据  将object类型的转换为Cart类型
        return carts.stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
    }

    /**
     * 修改
     * @param cart
     */
    @Override
    public void updateCarts(Cart cart) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        if(!this.redisTemplate.hasKey(key)){
            // 不存在，直接返回
            return ;
        }

        Integer num = cart.getNum();
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);
        // 获取购物车信息
        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        cart = JsonUtils.parse(cartJson, Cart.class);
        // 更新数量
        cart.setNum(num);
        // 写入购物车
        hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    @Override
    public void deleteCart(String skuId) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }
}
