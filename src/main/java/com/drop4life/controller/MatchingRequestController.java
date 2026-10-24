
package com.drop4life.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.drop4life.model.BloodRequest;
import com.drop4life.model.Donor;
import com.drop4life.model.User;
import com.drop4life.service.BloodRequestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MatchingRequestController {

    @Autowired
    private BloodRequestService bloodRequestService;

    @Autowired
    private SessionFactory sessionFactory;

    @GetMapping("/api/matchingRequests")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMatchingRequests(
            HttpSession session) {

        // Get the currently logged-in user
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please log in first");
        }

        Session dbSession = sessionFactory.getCurrentSession();

        // Check whether this user is registered as a donor
        Donor donor = dbSession.createQuery(
                "FROM Donor d WHERE d.userId = :userId",
                Donor.class)
                .setParameter("userId", user.getUserId())
                .uniqueResult();

        if (donor == null) {
            return List.of();
        }

        // Find blood requests matching the donor's blood group and city
        List<BloodRequest> requests =
                bloodRequestService.getMatchingRequests(
                        user.getBloodGroup(),
                        donor.getCity());

        // Prepare JSON response with requester contact details
        return requests.stream().map(request -> {

            Map<String, Object> item = new LinkedHashMap<>();

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

            // Fetch the user who submitted this blood request
            User requester = dbSession.get(
                    User.class,
                    request.getUserId());

            // Return the requester's registered phone number
            item.put(
                    "contactPhone",
                    requester != null ? requester.getPhone() : null);

            return item;

        }).toList();
    }
}
