package com.leyou.cart.controller;

import com.leyou.cart.domain.Cart;
import com.leyou.cart.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 加入购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){

        List<Cart> carts = this.cartService.queryCarts();
        if(CollectionUtils.isEmpty(carts)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改数量
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
        this.cartService.updateCarts(cart);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId) {
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
