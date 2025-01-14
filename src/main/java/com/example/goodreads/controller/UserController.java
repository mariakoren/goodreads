package com.example.goodreads.controller;

import com.example.goodreads.model.UserDtls;
import com.example.goodreads.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String userProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        UserDtls user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/edit")
    public String editUserProfileForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        UserDtls user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/edit")
    public String editUserProfile(
            @Valid UserDtls user,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            session.setAttribute("msg", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return "redirect:/user/edit";
        }

        String email = userDetails.getUsername();
        UserDtls existingUser = userService.getUserByEmail(email);

        existingUser.setFullName(user.getFullName());
        existingUser.setAddress(user.getAddress());

        userService.updateUser(existingUser);

        return "redirect:/user/";
    }

    @GetMapping("/confirmDelete")
    public String confirmDelete() {
        return "confirm-delete";
    }

    @GetMapping("/afterdelete")
    public String afterDelete() {
        return "after-delete";
    }

    @PostMapping("/delete")
    public String deleteAccount(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDtls user = userService.getUserByEmail(email);

        if (user != null) {
            userService.deleteUser(user.getId());
        }

        model.addAttribute("deleted", true);
        return "redirect:/user/afterdelete";
    }
}
