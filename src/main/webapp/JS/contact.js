document.addEventListener("DOMContentLoaded", function () {

    var contactForm = document.getElementById("contactForm");

    if (!contactForm) {
        console.error("Contact form not found.");
        return;
    }

    contactForm.addEventListener("submit", function (event) {
        event.preventDefault();

        var fullName = document.getElementById("fullName").value.trim();
        var email = document.getElementById("email").value.trim();
        var subject = document.getElementById("subject").value.trim();
        var message = document.getElementById("message").value.trim();

        if (!fullName || !email || !subject || !message) {
            alert("Please fill in all fields.");
            return;
        }

        var submitButton = contactForm.querySelector(
            'button[type="submit"]'
        );

        submitButton.disabled = true;
        submitButton.textContent = "Sending...";

        // Determine the application context path
        var currentPath = window.location.pathname;
        var htmlIndex = currentPath.indexOf("/HTML/");

        var contextPath = htmlIndex >= 0
            ? currentPath.substring(0, htmlIndex)
            : "";

        // Send the message to Spring MVC
        var xhr = new XMLHttpRequest();

        xhr.open("POST", contextPath + "/api/contact", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader("Accept", "application/json");

        xhr.onreadystatechange = function () {

            if (xhr.readyState !== 4) {
                return;
            }

            submitButton.disabled = false;
            submitButton.innerHTML =
                '<i class="fa-solid fa-paper-plane"></i> Send Message';

            var result = {};

            try {
                result = JSON.parse(xhr.responseText || "{}");
            } catch (error) {
                result = {};
            }

            if (xhr.status >= 200 && xhr.status < 300) {
                alert(
                    result.message ||
                    "Thank you! Your message was submitted successfully."
                );

                contactForm.reset();

            } else {
                console.error(
                    "Contact submission failed:",
                    xhr.status,
                    xhr.responseText
                );

                alert(
                    result.detail ||
                    result.message ||
                    "Unable to submit your message. Please try again."
                );
            }
        };

        xhr.onerror = function () {
            submitButton.disabled = false;
            submitButton.innerHTML =
                '<i class="fa-solid fa-paper-plane"></i> Send Message';

            alert("Network error. Please try again.");
        };

        xhr.send(JSON.stringify({
            fullName: fullName,
            email: email,
            subject: subject,
            message: message
        }));
    });
});