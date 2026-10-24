
package com.drop4life.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drop4life.dao.UserDAO;
import com.drop4life.model.User;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Transactional
    public boolean registerUser(User user) {

        if (userDAO.accountExists(
                user.getEmail(), user.getPhone())) {
            return false;
        }

        // Store a hashed password instead of plain text
        user.setPassword(
                passwordEncoder.encode(user.getPassword()));

        userDAO.saveUser(user);

        return true;
    }

    @Transactional
    public User loginUser(
            String email, String phone, String password) {

        User user = userDAO.findUserForLogin(email, phone);

        if (user == null || user.getPassword() == null) {
            return null;
        }

        String storedPassword = user.getPassword();

        // Verify passwords that are already BCrypt-hashed
        if (storedPassword.startsWith("$2a$")
                || storedPassword.startsWith("$2b$")
                || storedPassword.startsWith("$2y$")) {

            return passwordEncoder.matches(
                    password, storedPassword) ? user : null;
        }

        // Migrate an existing plain-text password after a
        // successful login. Hibernate saves the change
        // when the transaction commits.
        if (storedPassword.equals(password)) {
            user.setPassword(
                    passwordEncoder.encode(password));
            return user;
        }

        return null;
    }

    // Check whether an email is registered
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userDAO.findUserByEmail(email) != null;
    }

    // Save a new password securely
    @Transactional
    public boolean resetPassword(String email, String newPassword) {

        User user = userDAO.findUserByEmail(email);

        if (user == null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        return true;
    }

}
