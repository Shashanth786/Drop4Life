
package com.drop4life.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.drop4life.model.User;

@Service
public class NotificationEmailService {

    private static final Logger LOGGER =
            Logger.getLogger(NotificationEmailService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    // Confirmation email after a blood request is submitted
    public void sendBloodRequestConfirmation(User user) {

        String subject =
                "Your Blood Request Has Been Submitted - Drop4Life";

        String body =
                "Hello,\n\n"
                + "Thank you for using Drop4Life!\n\n"
                + "Your blood request has been submitted successfully. "
                + "We will help you connect with matching blood donors.\n\n"
                + "Please log in to your Drop4Life account to view "
                + "your request and matching donor information.\n\n"
                + "Thank you for being part of our community.\n\n"
                + "Regards,\n"
                + "Drop4Life Team";

        sendEmail(user, subject, body);
    }

    // Confirmation email after donor registration
    public void sendDonorRegistrationConfirmation(User user) {

        String subject =
                "Welcome to the Drop4Life Donor Community!";

        String body =
                "Hello,\n\n"
                + "Thank you for registering as a blood donor "
                + "with Drop4Life!\n\n"
                + "Your donor registration has been completed "
                + "successfully.\n\n"
                + "Thank you for your willingness to help save lives. "
                + "Your generosity can make a meaningful difference "
                + "to someone in need.\n\n"
                + "You can log in to Drop4Life to manage your donor "
                + "information and check matching blood requests.\n\n"
                + "Regards,\n"
                + "Drop4Life Team";

        sendEmail(user, subject, body);
    }

    // Shared email-sending method
    private void sendEmail(
            User user, String subject, String body) {

        if (user == null
                || user.getEmail() == null
                || user.getEmail().isBlank()) {

            LOGGER.warning(
                    "Confirmation email skipped: recipient email is missing.");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();

        String senderEmail = System.getenv("DROP4LIFE_EMAIL");

        if (senderEmail != null && !senderEmail.isBlank()) {
            message.setFrom(senderEmail);
        }

        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);

            LOGGER.info("Drop4Life confirmation email sent.");

        } catch (MailException e) {
            // A notification failure should not undo a successful
            // blood request or donor registration.
            LOGGER.log(
                    Level.WARNING,
                    "Drop4Life confirmation email could not be sent.",
                    e);
        }
    }
}
