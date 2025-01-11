package com.example.goodreads.controller;

import com.example.goodreads.model.UserDtls;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.goodreads.service.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/createuser")
    public String createUser(@ModelAttribute UserDtls user, HttpSession session ) {

        boolean f = userService.checkEmail(user.getEmail());

        if (f) {
            session.setAttribute("msg", "email is already exists");
        } else {
            UserDtls userDtls=userService.createUser(user);
            if (userDtls!=null){
                session.setAttribute("msg", "register successfully");

            } else {
                session.setAttribute("msg", "something went wrong");

            }

        }

        return "redirect:/register";

    }
}
