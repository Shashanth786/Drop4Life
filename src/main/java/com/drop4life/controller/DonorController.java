
package com.drop4life.controller;
import com.drop4life.service.NotificationEmailService;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drop4life.model.Donor;
import com.drop4life.model.User;
import com.drop4life.service.DonorService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DonorController {

    @Autowired
    private DonorService donorService;
    
    @Autowired
    private NotificationEmailService notificationEmailService;

    @PostMapping("/registerDonor")
    public String registerDonor(
            @RequestParam("age") int age,
            @RequestParam("city") String city,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login.html";
        }

        Donor donor = new Donor();
        donor.setUserId(user.getUserId());
        donor.setAge(age);
        donor.setCity(city.trim());

        boolean registered = donorService.registerDonor(donor);

        if (registered) {

            // Send confirmation email after successful registration
            notificationEmailService.sendDonorRegistrationConfirmation(user);

            return "redirect:/donorSuccess";
        }

        return "redirect:/HTML/donateblood.html?error=exists";
    }
    
    @GetMapping("/donorSuccess")
    public String donorSuccess() {
        return "donor-success";
    }
}
