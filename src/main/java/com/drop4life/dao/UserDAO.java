package com.drop4life.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drop4life.model.User;

@Repository
public class UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    // Save new user
    public void saveUser(User user) {

        Session session = sessionFactory.getCurrentSession();

        session.save(user);
    }

    // Check whether email or phone already exists
    public boolean accountExists(String email, String phone) {

        Session session = sessionFactory.getCurrentSession();

        String hql = "select count(u) from User u " +
                     "where u.email = :email or u.phone = :phone";

        Long count = session.createQuery(hql, Long.class)
                .setParameter("email", email)
                .setParameter("phone", phone)
                .uniqueResult();

        return count > 0;
    }
    
    

    // Find a user by registered email address
    public User findUserByEmail(String email) {

        Session session = sessionFactory.getCurrentSession();

        String hql = "from User u where lower(u.email) = :email";

        return session.createQuery(hql, User.class)
                .setParameter("email", email.trim().toLowerCase())
                .uniqueResult();
    }


    // Find the user whose email and phone match the same account
    public User findUserForLogin(String email, String phone) {

        Session session = sessionFactory.getCurrentSession();

        String hql = "from User u "
                   + "where u.email = :email "
                   + "and u.phone = :phone";

        return session.createQuery(hql, User.class)
                .setParameter("email", email.trim())
                .setParameter("phone", phone.trim())
                .uniqueResult();
    }


    
    
    public User loginUser(String email, String phone, String password) {

        Session session = sessionFactory.getCurrentSession();

        String hql = "from User u " +
                     "where (u.email = :email or u.phone = :phone) " +
                     "and u.password = :password";

        return session.createQuery(hql, User.class)
                .setParameter("email", email)
                .setParameter("phone", phone)
                .setParameter("password", password)
                .uniqueResult();
    }
}