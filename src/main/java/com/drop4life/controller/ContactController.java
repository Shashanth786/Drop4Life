package com.drop4life.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.drop4life.model.ContactMessage;

@Controller
public class ContactController {

    @Autowired
    private SessionFactory sessionFactory;

    @PostMapping("/api/contact")
    @ResponseBody
    @Transactional
    public Map<String, Object> submitContactMessage(
            @RequestBody Map<String, String> details) {

        String fullName = details.get("fullName");
        String email = details.get("email");
        String subject = details.get("subject");
        String message = details.get("message");

        if (fullName == null || email == null
                || subject == null || message == null
                || fullName.trim().isEmpty()
                || email.trim().isEmpty()
                || subject.trim().isEmpty()
                || message.trim().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please complete all contact form fields.");
        }

        fullName = fullName.trim();
        email = email.trim();
        subject = subject.trim();
        message = message.trim();

        if (fullName.length() > 100
                || email.length() > 150
                || subject.length() > 200
                || message.length() > 10000) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One or more fields exceed the allowed length.");
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please enter a valid email address.");
        }

        ContactMessage contact = new ContactMessage();
        contact.setFullName(fullName);
        contact.setEmail(email);
        contact.setSubject(subject);
        contact.setMessage(message);

        Session dbSession = sessionFactory.getCurrentSession();
        dbSession.persist(contact);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message",
                "Your message has been submitted successfully.");
        response.put("messageId", contact.getMessageId());

        return response;
    }
}