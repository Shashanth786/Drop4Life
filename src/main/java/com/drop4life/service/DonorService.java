
package com.drop4life.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drop4life.dao.DonorDAO;
import com.drop4life.model.Donor;

@Service
public class DonorService {

    @Autowired
    private DonorDAO donorDAO;

    @Transactional
    public boolean registerDonor(Donor donor) {

        // Prevent duplicate donor registration
        if (donorDAO.donorExists(donor.getUserId())) {
            return false;
        }

        // Save donor details
        donorDAO.saveDonor(donor);

        return true;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMatchingDonors(
            String bloodGroup, String city) {

        return donorDAO.findMatchingDonors(bloodGroup, city);
    }
}
