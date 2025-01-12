package com.example.goodreads.service;

import com.example.goodreads.model.UserDtls;

public interface UserService {

    UserDtls createUser(UserDtls user);

    boolean checkEmail(String email);

    UserDtls getUserByEmail(String email);

    void updateUser(UserDtls user);

    void deleteUser(int userId);
}
