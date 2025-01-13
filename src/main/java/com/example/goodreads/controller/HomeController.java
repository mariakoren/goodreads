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
    public String createUser(@ModelAttribute UserDtls user, HttpSession session) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        if (!user.getEmail().matches(emailRegex)) {
            session.setAttribute("msg", "Invalid email format");
            return "redirect:/register";
        }
        if (user.getPassword().isEmpty()) {
            session.setAttribute("msg", "Password is empty");
            return "redirect:/register";
        } else if (user.getPassword().length()<3) {
            session.setAttribute("msg", "Password must be at least 3 characters");
            return "redirect:/register";
        }
        if (user.getFullName().isEmpty()) {
            session.setAttribute("msg", "Full name is empty");
            return "redirect:/register";
        }
        if (user.getAddress().isEmpty()) {
            session.setAttribute("msg", "Address is empty");
            return "redirect:/register";
        }
        boolean emailExists = userService.checkEmail(user.getEmail());
        if (emailExists) {
            session.setAttribute("msg", "Email already exists");
            return "redirect:/register";
        } else {
            UserDtls userDtls = userService.createUser(user);
            if (userDtls != null) {
                session.setAttribute("msg", "Registered successfully");
            } else {
                session.setAttribute("msg", "Something went wrong");
            }
        }

        return "redirect:/register";
    }
}
