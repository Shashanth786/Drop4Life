
package com.drop4life.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drop4life.dao.BloodRequestDAO;
import com.drop4life.model.BloodRequest;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestDAO bloodRequestDAO;

    @Transactional(readOnly = true)
    public List<BloodRequest> getMatchingRequests(String bloodGroup, String city) {
        return bloodRequestDAO.findMatchingRequests(bloodGroup, city);
    }

    // NEW METHOD: Save a blood request
    @Transactional
    public void saveBloodRequest(BloodRequest bloodRequest) {
        bloodRequestDAO.saveBloodRequest(bloodRequest);
    }
}
