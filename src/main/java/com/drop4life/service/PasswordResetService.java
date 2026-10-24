
package com.drop4life.service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    private static final long OTP_VALIDITY =
            5 * 60 * 1000L;

    private static final long RESEND_COOLDOWN =
            60 * 1000L;

    private static final long VERIFIED_VALIDITY =
            10 * 60 * 1000L;

    private static final int MAX_ATTEMPTS = 5;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    private final SecureRandom random = new SecureRandom();

    private final Map<String, OtpRecord> otpRecords =
            new HashMap<>();

    private final Map<String, Long> lastSentTimes =
            new HashMap<>();

    private static class OtpRecord {
        String otp;
        long expiresAt;
        long verifiedUntil;
        int attempts;

        OtpRecord(String otp, long expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
            this.verifiedUntil = 0;
            this.attempts = 0;
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    // Generate and send a six-digit OTP
    public synchronized boolean sendOtp(String email) {

        email = normalizeEmail(email);

        if (!userService.emailExists(email)) {
            // Do not reveal whether an account exists
            return true;
        }

        long now = System.currentTimeMillis();

        Long lastSent = lastSentTimes.get(email);

        if (lastSent != null
                && now - lastSent < RESEND_COOLDOWN) {
            return false;
        }

        String otp = String.format(
                "%06d", random.nextInt(1_000_000));

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(System.getenv("DROP4LIFE_EMAIL"));
        message.setTo(email);
        message.setSubject("Drop4Life Password Reset OTP");
        message.setText(
                "Hello,\n\n"
                + "Your Drop4Life password reset OTP is: "
                + otp + "\n\n"
                + "This OTP expires in 5 minutes.\n"
                + "Do not share this code with anyone.\n\n"
                + "If you did not request this, ignore this email.\n\n"
                + "Regards,\nDrop4Life Team"
        );

        // Save the OTP only after the email sends successfully
        mailSender.send(message);

        otpRecords.put(
                email,
                new OtpRecord(otp, now + OTP_VALIDITY));

        lastSentTimes.put(email, now);

        return true;
    }

    // Verify the OTP
    public synchronized boolean verifyOtp(
            String email, String enteredOtp) {

        email = normalizeEmail(email);

        OtpRecord record = otpRecords.get(email);

        if (record == null
                || record.otp == null
                || System.currentTimeMillis() > record.expiresAt
                || record.attempts >= MAX_ATTEMPTS) {

            otpRecords.remove(email);
            return false;
        }

        record.attempts++;

        if (record.otp.equals(enteredOtp)) {

            record.otp = null;
            record.verifiedUntil =
                    System.currentTimeMillis()
                    + VERIFIED_VALIDITY;

            return true;
        }

        if (record.attempts >= MAX_ATTEMPTS) {
            otpRecords.remove(email);
        }

        return false;
    }

    // Change the password only after successful OTP verification
    public synchronized boolean resetPassword(
            String email, String newPassword) {

        email = normalizeEmail(email);

        OtpRecord record = otpRecords.get(email);

        if (record == null
                || record.verifiedUntil == 0
                || System.currentTimeMillis() > record.verifiedUntil) {

            return false;
        }

        boolean updated =
                userService.resetPassword(email, newPassword);

        if (updated) {
            otpRecords.remove(email);
            lastSentTimes.remove(email);
        }

        return updated;
    }
}
