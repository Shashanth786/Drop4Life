
package com.drop4life.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drop4life.service.PasswordResetService;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    // Step 1: Send OTP to the registered email
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOtp(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");

        if (email == null
                || email.trim().isEmpty()
                || !email.trim().contains("@")) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "Enter a valid email address."
                    )
            );
        }

        try {
            boolean sent = passwordResetService.sendOtp(email);

            if (!sent) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(Map.of(
                                "success", false,
                                "message",
                                "Please wait 60 seconds before requesting another OTP."
                        ));
            }

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "If this email is registered, an OTP has been sent."
                    )
            );

        } catch (MailException e) {
        	 e.printStackTrace(); // Temporary debugging
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "success", false,
                            "message",
                            "Unable to send the email. Check the mail configuration and try again."
                    ));
        }
    }

    // Step 2: Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || email.trim().isEmpty()
                || otp == null || otp.trim().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "Email and OTP are required."
                    )
            );
        }

        boolean verified =
                passwordResetService.verifyOtp(email, otp.trim());

        if (!verified) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "Invalid or expired OTP. Request a new one."
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "OTP verified successfully."
                )
        );
    }

    // Step 3: Reset password after OTP verification
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");
        String newPassword = request.get("newPassword");

        if (email == null || email.trim().isEmpty()
                || newPassword == null
                || newPassword.length() < 8
                || newPassword.length() > 72) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message",
                            "Enter a new password between 8 and 72 characters."
                    )
            );
        }

        boolean reset =
                passwordResetService.resetPassword(email, newPassword);

        if (!reset) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message",
                            "OTP verification is required or has expired. Please verify again."
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message",
                        "Password reset successfully. You can now log in."
                )
        );
    }
}
