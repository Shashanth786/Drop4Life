

package com.drop4life.controller;

import java.util.List;
import com.drop4life.model.BloodRequest;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.drop4life.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private SessionFactory sessionFactory;

    // Get the currently logged-in user's profile
    @GetMapping("/api/profile")
    @ResponseBody
    @Transactional(readOnly = true)
    public Map<String, Object> getProfile(HttpSession httpSession) {

        User loggedInUser =
                (User) httpSession.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please log in first");
        }

        Session dbSession = sessionFactory.getCurrentSession();

        User user = dbSession.get(
                User.class, loggedInUser.getUserId());

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User profile not found");
        }

        return createProfileResponse(user);
    }

    // Update the logged-in user's permitted profile details
    @PostMapping("/api/profile/update")
    @ResponseBody
    @Transactional
    public Map<String, Object> updateProfile(
            @RequestBody Map<String, String> details,
            HttpSession httpSession) {

        User loggedInUser =
                (User) httpSession.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please log in first");
        }

        String name = details.get("fullName");
        String email = details.get("email");
        String phone = details.get("phone");

        if (name == null || name.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name, email and phone are required");
        }

        name = name.trim();
        email = email.trim();
        phone = phone.trim();

         if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please enter a valid email address");
        }

        Session dbSession = sessionFactory.getCurrentSession();

        User user = dbSession.get(
                User.class, loggedInUser.getUserId());

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User profile not found");
        }

        // Prevent changing to another user's email address
        Long duplicateEmail = dbSession.createQuery(
                "select count(u) from User u "
                + "where lower(u.email) = lower(:email) "
                + "and u.userId <> :userId",
                Long.class)
                .setParameter("email", email)
                .setParameter("userId", user.getUserId())
                .uniqueResult();

        if (duplicateEmail != null && duplicateEmail > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This email address is already registered");
        }

        // Update only the permitted fields
        user.setFullName(name);
        user.setEmail(email);
        user.setPhone(phone);

        dbSession.merge(user);

        // Keep the current session consistent with the database
        httpSession.setAttribute("loggedInUser", user);

        Map<String, Object> response = createProfileResponse(user);
        response.put("message", "Profile updated successfully");

        return response;
    }

    // Return safe profile fields only. Never return the password.
    private Map<String, Object> createProfileResponse(User user) {

        Map<String, Object> profile = new LinkedHashMap<>();

        profile.put("userId", user.getUserId());
        profile.put("fullName", user.getFullName());
        profile.put("email", user.getEmail());
        profile.put("phone", user.getPhone());
        profile.put("bloodGroup", user.getBloodGroup());

        return profile;
    }
 // Get blood requests submitted by the logged-in user
    @GetMapping("/api/myBloodRequests")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMyBloodRequests(
            HttpSession httpSession) {

        User loggedInUser =
                (User) httpSession.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please log in first");
        }

        Session dbSession = sessionFactory.getCurrentSession();

        List<BloodRequest> requests = dbSession.createQuery(
                "FROM BloodRequest b WHERE b.userId = :userId "
                + "ORDER BY b.requestId DESC",
                BloodRequest.class)
                .setParameter("userId", loggedInUser.getUserId())
                .getResultList();

        return requests.stream().map(request -> {
            Map<String, Object> item = new LinkedHashMap<>();

            item.put("requestId", request.getRequestId());
            item.put("patientName", request.getPatientName());
            item.put("bloodGroup", request.getBloodGroup());
            item.put("city", request.getCity());
            item.put("hospitalName", request.getHospitalName());

            return item;
        }).toList();
    }
    @PostMapping("/api/logout")
    @ResponseBody
    public Map<String, String> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return Map.of("message", "Logged out successfully");
    }
}