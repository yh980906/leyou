package com.leyou.cart.service;

import com.leyou.cart.domain.Cart;

import java.util.List;

public interface ICartService {
    void addCart(Cart cart);

    List<Cart> queryCarts();

    void updateCarts(Cart cart);

    void deleteCart(String skuId);
}
