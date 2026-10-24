
package com.drop4life.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drop4life.model.Donor;
import com.drop4life.model.User;

@Repository
public class DonorDAO {

    @Autowired
    private SessionFactory sessionFactory;

    // Check whether the user is already registered as a donor
    public boolean donorExists(int userId) {

        Session session = sessionFactory.getCurrentSession();

        Long count = session.createQuery(
                "select count(d) from Donor d where d.userId = :userId",
                Long.class)
                .setParameter("userId", userId)
                .uniqueResult();

        return count != null && count > 0;
    }

    // Save donor details
    public void saveDonor(Donor donor) {

        Session session = sessionFactory.getCurrentSession();

        session.persist(donor);
    }

    // Find donors matching the requested blood group and city
    public List<Map<String, Object>> findMatchingDonors(
            String bloodGroup, String city) {

        Session session = sessionFactory.getCurrentSession();

        List<Object[]> rows = session.createQuery(
                "select u.fullName, u.phone, u.bloodGroup, d.city, d.age " +
                "from Donor d, User u " +
                "where d.userId = u.userId " +
                "and upper(trim(u.bloodGroup)) = upper(trim(:bloodGroup)) " +
                "and lower(trim(d.city)) = lower(trim(:city))",
                Object[].class)
                .setParameter("bloodGroup", bloodGroup)
                .setParameter("city", city)
                .getResultList();

        List<Map<String, Object>> donors = new ArrayList<>();

        for (Object[] row : rows) {

            Map<String, Object> donor = new LinkedHashMap<>();

            donor.put("fullName", row[0]);
            donor.put("phone", row[1]);
            donor.put("bloodGroup", row[2]);
            donor.put("city", row[3]);
            donor.put("age", row[4]);

            donors.add(donor);
        }

        return donors;
    }
}
