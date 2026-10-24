
package com.drop4life.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drop4life.model.User;
import com.drop4life.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute User user,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session) {

        if (!user.getPassword().equals(confirmPassword)) {
            return "redirect:/signup.html?error=password";
        }

        boolean registered = userService.registerUser(user);

        if (registered) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/HTML/a.jsp";
        }

        return "redirect:/signup.html?error=exists";
    }
}
