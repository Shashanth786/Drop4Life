
package com.drop4life.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drop4life.model.User;
import com.drop4life.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            HttpSession session) {

        User user = userService.loginUser(email, phone, password);

        if (user != null) {

            // Store the authenticated user in the session
            session.setAttribute("loggedInUser", user);

            // Redirect according to the role stored in the database
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/HTML/admin-dashboard.html";
            } 

            return "redirect:/HTML/a.jsp";
        }

        return "redirect:/login.html?error=invalid";
    }
}
