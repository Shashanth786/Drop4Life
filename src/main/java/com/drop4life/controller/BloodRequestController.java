
package com.drop4life.controller;

import com.drop4life.service.NotificationEmailService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.drop4life.model.BloodRequest;
import com.drop4life.model.User;
import com.drop4life.service.BloodRequestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BloodRequestController {

    @Autowired
    private BloodRequestService bloodRequestService;
    
    @Autowired
    private NotificationEmailService notificationEmailService;

    @PostMapping("/submitBloodRequest")
    public String submitBloodRequest(
            @RequestParam("patientName") String patientName,
            @RequestParam("patientAge") int patientAge,
            @RequestParam("patientGender") String patientGender,
            @RequestParam("bloodGroup") String bloodGroup,
            @RequestParam("unitsRequired") int unitsRequired,
            @RequestParam("urgency") String urgency,
            @RequestParam("city") String city,
            @RequestParam("hospitalName") String hospitalName,
            @RequestParam("hospitalAddress") String hospitalAddress,
            @RequestParam("purpose") String purpose,
            @RequestParam(value = "hospitalReceipt", required = false)
            MultipartFile hospitalReceipt,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        System.out.println("SUBMIT BLOOD REQUEST CONTROLLER CALLED");

        // Check whether the user is logged in
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login.html";
        }

        // Create the blood request
        BloodRequest request = new BloodRequest();

        request.setUserId(user.getUserId());
        request.setPatientName(patientName);
        request.setPatientAge(patientAge);
        request.setPatientGender(patientGender);
        request.setBloodGroup(bloodGroup);
        request.setUnitsRequired(unitsRequired);
        request.setUrgency(urgency);
        request.setCity(city.trim());
        request.setHospitalName(hospitalName);
        request.setHospitalAddress(hospitalAddress);
        request.setPurpose(purpose);

        // Save the receipt only when a file is uploaded
        if (hospitalReceipt != null && !hospitalReceipt.isEmpty()) {
            try {
                String originalFilename = hospitalReceipt.getOriginalFilename();

                String extension = "";
                if (originalFilename != null && originalFilename.lastIndexOf('.') >= 0) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                }

                String filename = UUID.randomUUID().toString() + extension;

                Path uploadDirectory = Paths.get(
                        System.getProperty("user.home"),
                        "Drop4LifeUploads"
                );

                Files.createDirectories(uploadDirectory);

                Path destination = uploadDirectory.resolve(filename).normalize();

                if (!destination.getParent().equals(uploadDirectory.toAbsolutePath().normalize())
                        && !destination.getParent().equals(uploadDirectory.normalize())) {
                    redirectAttributes.addFlashAttribute(
                            "errorMessage", "Invalid receipt file path.");
                    return "redirect:/HTML/requestblood.html";
                }

                Files.copy(
                        hospitalReceipt.getInputStream(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );

                request.setHospitalReceipt(filename);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Receipt upload failed. Please try again.");
                return "redirect:/HTML/requestblood.html";
            }
        } else {
            // Receipt is optional
            request.setHospitalReceipt(null);
        }

        // Save the request to the database
        bloodRequestService.saveBloodRequest(request);
        
        notificationEmailService.sendBloodRequestConfirmation(user);

        redirectAttributes.addAttribute("requestId", request.getRequestId());

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Request submitted successfully!");

        return "redirect:/requestSuccess";
    }

    @GetMapping("/requestSuccess")
    public String requestSuccess() {
        return "success";
    }
}
