
package com.drop4life.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drop4life.model.BloodRequest;

@Repository
public class BloodRequestDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<BloodRequest> findMatchingRequests(String bloodGroup, String city) {

        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                "FROM BloodRequest b WHERE b.bloodGroup = :bloodGroup " +
                "AND LOWER(TRIM(b.city)) = LOWER(TRIM(:city))",
                BloodRequest.class)
                .setParameter("bloodGroup", bloodGroup)
                .setParameter("city", city)
                .getResultList();
    }

    // NEW METHOD: Save a blood request into the database
    public void saveBloodRequest(BloodRequest bloodRequest) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(bloodRequest);
    }
    
    
 // Find one blood request by its ID
 public BloodRequest findById(int requestId) {

     Session session = sessionFactory.getCurrentSession();

     return session.get(BloodRequest.class, requestId);
 }
 
}
