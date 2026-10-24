<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%
    // Prevent the browser from reusing a cached dashboard
    response.setHeader("Cache-Control",
            "no-store, no-cache, must-revalidate, max-age=0");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // Only logged-in users can access the dashboard
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect(
                request.getContextPath() + "/login.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Drop4Life - Dashboard</title>

    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">

    <!-- Google Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap"
          rel="stylesheet">

    <!-- Dashboard CSS --> 
    <link rel="stylesheet" href="../CSS/a.css">

    <link rel="stylesheet" href="../CSS/responsive.css">
    <script src="../JS/mobile-nav.js" defer></script>
</head>


<body>


<!-- ================= NAVBAR ================= -->

 
   

<nav class="dashboard-navbar">

    <div class="dashboard-logo">

        <i class="fa-solid fa-droplet"></i>

        <span>
            DROP<span class="logo-four">4</span>LIFE
        </span>

    </div>


    <div class="dashboard-nav-right">

        <a href="profile.html" class="profile-link">

            <i class="fa-solid fa-circle-user"></i>

            My Profile

        </a>

        <button id="logoutBtn" class="logout-btn">

            <i class="fa-solid fa-right-from-bracket"></i>

            Logout

        </button>

    </div>

</nav>


<!-- Welcome Section: place immediately after the navbar -->
<section class="welcome-section">
    <div class="welcome-content">
        <div class="welcome-icon">
           <span aria-hidden="true">&hearts;</span>
        </div>

        <div class="welcome-text">
            <span class="welcome-label">YOUR DROP4LIFE DASHBOARD</span>
            <h1>Welcome, ${sessionScope.loggedInUser.fullName}!</h1>
            <p>
                Every drop counts. Thank you for being part of a
                community that helps save lives.
            </p>
        </div>
    </div>

    <div class="welcome-decoration">
      
    </div>
</section>




<!-- ================= HERO ================= -->

<section class="dashboard-hero">

    <div class="hero-overlay"></div>

    <div class="hero-content">

        <p class="hero-small">
            WELCOME TO DROP4LIFE
        </p>

        <h1>
            Every Drop Can
            <span>Save a Life</span>
        </h1>

        <p class="hero-description">

            Connect with blood donors and help patients
            find the blood they need when it matters most.

        </p>


        <!-- Main Buttons -->

        <div class="hero-buttons">

            <a href="requestblood.html"
               class="hero-btn request-btn">

                <i class="fa-solid fa-hand-holding-droplet"></i>

                Request Blood

            </a>


            <a href="donateblood.html"
               class="hero-btn donate-btn">

                <i class="fa-solid fa-heart-circle-plus"></i>

                Donate Blood

            </a>

        </div>

    </div>

     <!-- RIGHT SIDE IMAGE -->
    <div class="hero-cycle">

        <img src="../Images/Cycle.png"
             alt="Blood Donation Cycle">

    </div>

</section>



<!-- ================= BLOOD INFORMATION ================= -->

<section class="blood-info">

    <div class="section-heading">

        <p class="section-tag">
            BLOOD DONATION
        </p>

        <h2>
            Your One Decision Can Make a Difference
        </h2>

        <p>
            Blood cannot be manufactured. It can only come from generous
            donors like you.
        </p>

    </div>



    <!-- Information Cards -->

    <div class="info-cards">


        <div class="info-card">

            <div class="info-icon">

                <i class="fa-solid fa-droplet"></i>

            </div>

            <h3>
                One Donation
            </h3>

            <p>
                A single blood donation can help patients
                who need blood during emergencies and treatments.
            </p>

        </div>



        <div class="info-card">

            <div class="info-icon">

                <i class="fa-solid fa-heart-pulse"></i>

            </div>

            <h3>
                Save Lives
            </h3>

            <p>
                Your blood donation can become a vital part
                of someone's journey toward recovery.
            </p>

        </div>



        <div class="info-card">

            <div class="info-icon">

                <i class="fa-solid fa-people-arrows"></i>

            </div>

            <h3>
                Connect People
            </h3>

            <p>
                Drop4Life connects people who need blood
                with donors who are ready to help.
            </p>

        </div>

    </div>



    <!-- Blood Facts -->

    <div class="blood-facts">


        <div class="fact">

            <i class="fa-solid fa-user-group"></i>

            <div>

                <strong>
                    Donors
                </strong>

                <span>
                    People ready to help
                </span>

            </div>

        </div>



        <div class="fact">

            <i class="fa-solid fa-hospital"></i>

            <div>

                <strong>
                    Hospitals
                </strong>

                <span>
                    Where patients receive care
                </span>

            </div>

        </div>



        <div class="fact">

            <i class="fa-solid fa-phone-volume"></i>

            <div>

                <strong>
                    Direct Contact
                </strong>

                <span>
                    Connect with available donors
                </span>

            </div>

        </div>


    </div>

</section>



<!-- ================= CALL TO ACTION ================= -->

<section class="dashboard-cta">

    <div>

        <h2>
            Someone's Life May Depend On You
        </h2>

        <p>
            Become an available donor and help someone
            find the blood they need.
        </p>

    </div>


    <a href="donateblood.html"
       class="cta-btn">

        Become a Donor

        <i class="fa-solid fa-arrow-right"></i>

    </a>

</section>



<!-- ================= FOOTER ================= -->

<footer class="dashboard-footer">

    <div class="footer-logo">

        <i class="fa-solid fa-droplet"></i>

        DROP<span>4</span>LIFE

    </div>


    <p>
        Every Drop Counts. Every Life Matters.
    </p>
    
   <p class="copyright">

        Â© 2026 Drop4Life. All Rights Reserved.

    </p>

</footer>


 <script>
    const contextPath = "${pageContext.request.contextPath}";
    const logoutBtn = document.getElementById("logoutBtn");

    // Dashboard logout
    logoutBtn.addEventListener("click", async function () {
        logoutBtn.disabled = true;
        logoutBtn.innerHTML =
            '<i class="fa-solid fa-spinner fa-spin"></i> Logging out...';

        try {
            const response = await fetch(
                contextPath + "/api/logout",
                {
                    method: "POST",
                    credentials: "same-origin",
                    cache: "no-store",
                    headers: {
                        "Accept": "application/json"
                    }
                }
            );

            if (!response.ok) {
                throw new Error("Logout failed.");
            }

            // Replace dashboard history with the login page
            window.location.replace(
                contextPath + "/login.html"
            );

        } catch (error) {
            console.error("Logout error:", error);

            alert("Unable to log out. Please try again.");

            logoutBtn.disabled = false;
            logoutBtn.innerHTML =
                '<i class="fa-solid fa-right-from-bracket"></i> Logout';
        }
    });

    // Check the session whenever the dashboard is displayed,
    // including when the browser Back button restores the page.
    window.addEventListener("pageshow", async function () {
        try {
            const response = await fetch(
                contextPath + "/api/profile",
                {
                    method: "GET",
                    credentials: "same-origin",
                    cache: "no-store",
                    headers: {
                        "Accept": "application/json"
                    }
                }
            );

            if (response.status === 401) {
                window.location.replace(
                    contextPath + "/login.html"
                );
            }
        } catch (error) {
            console.error("Session verification failed:", error);
        }
    });
</script>
    
    

</body>

</html>