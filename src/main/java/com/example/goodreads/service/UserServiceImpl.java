package com.example.goodreads.service;

import com.example.goodreads.model.UserDtls;
import com.example.goodreads.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDtls createUser(UserDtls user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepo.save(user);
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public UserDtls getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public void updateUser(UserDtls user) {
        userRepo.save(user);
    }
}
