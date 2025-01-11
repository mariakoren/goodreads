package com.example.goodreads.service;

import com.example.goodreads.model.UserDtls;

public interface UserService {

    public UserDtls createUser(UserDtls user);

    public boolean checkEmail(String email);
}
