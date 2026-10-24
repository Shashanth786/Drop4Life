
package com.drop4life.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.drop4life.dao.BloodRequestDAO;
import com.drop4life.model.BloodRequest;
import com.drop4life.model.User;
import com.drop4life.service.DonorService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DonorAvailabilityController {

    @Autowired
    private BloodRequestDAO bloodRequestDAO;

    @Autowired
    private DonorService donorService;

    @GetMapping("/api/matchingDonors")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMatchingDonors(
            @RequestParam("requestId") int requestId,
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please log in first");
        }

        BloodRequest request = bloodRequestDAO.findById(requestId);

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Blood request not found");
        }

        // Allow users to view donors only for their own request
        if (request.getUserId() != loggedInUser.getUserId()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot view donors for this request");
        }

        return donorService.getMatchingDonors(
                request.getBloodGroup(),
                request.getCity());
    }
}
