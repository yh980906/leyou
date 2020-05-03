package com.leyou.user.service;

import com.leyou.user.domain.User;

public interface IUserService {
    Boolean checkUser(String data, Integer type);

    void sendVerifyCode(String phone);

    Boolean register(User user, String code);

    User queryUser(String username, String password);
}
