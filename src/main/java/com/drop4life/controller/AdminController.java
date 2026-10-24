package com.drop4life.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.drop4life.model.BloodRequest;
import com.drop4life.model.ContactMessage;
import com.drop4life.model.Donor;
import com.drop4life.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @Autowired
    private SessionFactory sessionFactory;

    // Verify admin role from the database on every admin operation
    private User requireAdmin(HttpSession httpSession, Session dbSession) {

        User loggedInUser =
                (User) httpSession.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please log in first");
        }

        User currentUser = dbSession.get(
                User.class, loggedInUser.getUserId());

        if (currentUser == null
                || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Administrator access required");
        }

        return currentUser;
    }

    // 1. Dashboard summary counts
    @GetMapping("/api/admin/summary")
    @ResponseBody
    @Transactional(readOnly = true)
    public Map<String, Long> getSummary(HttpSession httpSession) {

        Session dbSession = sessionFactory.getCurrentSession();
        requireAdmin(httpSession, dbSession);

        Map<String, Long> summary = new LinkedHashMap<>();

        summary.put("users",
                dbSession.createQuery(
                        "SELECT COUNT(u) FROM User u", Long.class)
                        .getSingleResult());

        summary.put("donors",
                dbSession.createQuery(
                        "SELECT COUNT(d) FROM Donor d", Long.class)
                        .getSingleResult());

        summary.put("bloodRequests",
                dbSession.createQuery(
                        "SELECT COUNT(b) FROM BloodRequest b", Long.class)
                        .getSingleResult());

        summary.put("contactMessages",
                dbSession.createQuery(
                        "SELECT COUNT(c) FROM ContactMessage c",
                        Long.class).getSingleResult());

        return summary;
    }

    // 2. View all registered users
    @GetMapping("/api/admin/users")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUsers(
            HttpSession httpSession) {

        Session dbSession = sessionFactory.getCurrentSession();
        requireAdmin(httpSession, dbSession);

        List<User> users = dbSession.createQuery(
                "FROM User u ORDER BY u.userId DESC",
                User.class).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> item = new LinkedHashMap<>();

            item.put("userId", user.getUserId());
            item.put("fullName", user.getFullName());
            item.put("email", user.getEmail());
            item.put("phone", user.getPhone());
            item.put("bloodGroup", user.getBloodGroup());
            item.put("role", user.getRole());

            // Never expose passwords
            result.add(item);
        }

        return result;
    }

    // 3. View registered donors
    @GetMapping("/api/admin/donors")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDonors(
            HttpSession httpSession) {

        Session dbSession = sessionFactory.getCurrentSession();
        requireAdmin(httpSession, dbSession);

        List<Donor> donors = dbSession.createQuery(
                "FROM Donor d ORDER BY d.donorId DESC",
                Donor.class).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Donor donor : donors) {

            User user = dbSession.get(
                    User.class, donor.getUserId());

            Map<String, Object> item = new LinkedHashMap<>();

            item.put("userId", donor.getUserId());
            item.put("donorId", donor.getDonorId());
            item.put("age", donor.getAge());
            item.put("city", donor.getCity());

            if (user != null) {
                item.put("fullName", user.getFullName());
                item.put("email", user.getEmail());
                item.put("phone", user.getPhone());
                item.put("bloodGroup", user.getBloodGroup());
            }

            result.add(item);
        }

        return result;
    }

    // 4. View all blood requests
    @GetMapping("/api/admin/bloodRequests")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBloodRequests(
            HttpSession httpSession) {

        Session dbSession = sessionFactory.getCurrentSession();
        requireAdmin(httpSession, dbSession);

        List<BloodRequest> requests = dbSession.createQuery(
                "FROM BloodRequest b ORDER BY b.requestId DESC",
                BloodRequest.class).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (BloodRequest request : requests) {

            User requester = dbSession.get(
                    User.class, request.getUserId());

            Map<String, Object> item = new LinkedHashMap<>();

            item.put("requestId", request.getRequestId());
            item.put("userId", request.getUserId());
            item.put("patientName", request.getPatientName());
            item.put("patientAge", request.getPatientAge());
            item.put("patientGender", request.getPatientGender());
            item.put("bloodGroup", request.getBloodGroup());
            item.put("unitsRequired", request.getUnitsRequired());
            item.put("urgency", request.getUrgency());
            item.put("city", request.getCity());
            item.put("hospitalName", request.getHospitalName());
            item.put("hospitalAddress", request.getHospitalAddress());
            item.put("purpose", request.getPurpose());

            if (requester != null) {
                item.put("requesterName", requester.getFullName());
                item.put("requesterEmail", requester.getEmail());
                item.put("requesterPhone", requester.getPhone());
            }

            result.add(item);
        }

        return result;
    }

    // 5. View Contact Us messages
    @GetMapping("/api/admin/contactMessages")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getContactMessages(
            HttpSession httpSession) {

        Session dbSession = sessionFactory.getCurrentSession();
        requireAdmin(httpSession, dbSession);

        List<ContactMessage> messages = dbSession.createQuery(
                "FROM ContactMessage c ORDER BY c.messageId DESC",
                ContactMessage.class).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (ContactMessage message : messages) {

            Map<String, Object> item = new LinkedHashMap<>();

            item.put("messageId", message.getMessageId());
            item.put("fullName", message.getFullName());
            item.put("email", message.getEmail());
            item.put("subject", message.getSubject());
            item.put("message", message.getMessage());
            item.put("submittedAt",
                    message.getSubmittedAt() == null
                            ? null
                            : message.getSubmittedAt().toString());

            result.add(item);
        }

        return result;
    }

    // 6. Delete a contact message
    @DeleteMapping("/api/admin/contactMessages/{messageId}")
    @ResponseBody
    @Transactional
    public Map<String, String> deleteContactMessage(
            @PathVariable("messageId") int messageId,
            HttpSession httpSession) {

        Session dbSession = sessionFactory.getCurrentSession();
        requireAdmin(httpSession, dbSession);

        ContactMessage message = dbSession.get(
                ContactMessage.class, messageId);

        if (message == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Contact message not found");
        }

        dbSession.remove(message);

        return Map.of("message",
                "Contact message deleted successfully");
    }

    // 7. Delete an ordinary user and associated donor/request records
    @DeleteMapping("/api/admin/users/{userId}")
    @ResponseBody
    @Transactional
    public Map<String, String> deleteUser(
            @PathVariable("userId") int userId,
            HttpSession httpSession) {

        Session dbSession = sessionFactory.getCurrentSession();
        User admin = requireAdmin(httpSession, dbSession);

        if (admin.getUserId() == userId) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot delete your own administrator account");
        }

        User target = dbSession.get(User.class, userId);

        if (target == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found");
        }

        if ("ADMIN".equalsIgnoreCase(target.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Administrator accounts cannot be deleted here");
        }

        // Remove associated records before deleting the user
        dbSession.createMutationQuery(
                "DELETE FROM BloodRequest b WHERE b.userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();

        dbSession.createMutationQuery(
                "DELETE FROM Donor d WHERE d.userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();

        dbSession.remove(target);

        return Map.of("message",
                "User and associated records deleted successfully");
    }
}