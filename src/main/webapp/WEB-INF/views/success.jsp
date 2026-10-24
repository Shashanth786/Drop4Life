<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blood Request Submitted | Drop4Life</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f5f7fa;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            color: #252525;
        }

        .navbar {
            background: #fff;
            padding: 20px 8%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
        }

        .logo {
            color: #d90429;
            font-size: 27px;
            font-weight: bold;
            text-decoration: none;
        }

        .nav-links a {
            color: #333;
            text-decoration: none;
            margin-left: 24px;
            font-size: 15px;
        }

        .nav-links a:hover {
            color: #d90429;
        }

        .success-section {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 50px 20px;
        }

        .success-card {
            width: 100%;
            max-width: 600px;
            background: #fff;
            text-align: center;
            padding: 45px 35px;
            border-radius: 18px;
            box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
        }

        .success-icon {
            width: 90px;
            height: 90px;
            margin: 0 auto 24px;
            background: #e5f8ed;
            color: #198754;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 45px;
        }

        h1 {
            font-size: 29px;
            margin-bottom: 18px;
            color: #222;
        }

        .message {
            font-size: 16px;
            color: #666;
            line-height: 1.8;
            margin-bottom: 28px;
        }

        .btn {
            display: inline-block;
            padding: 14px 22px;
            margin: 6px;
            background: #d90429;
            color: #fff;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
            transition: background 0.2s;
        }

        .btn:hover {
            background: #b90322;
        }

        .btn-secondary {
            background: #343a40;
        }

        .btn-secondary:hover {
            background: #22272b;
        }

        footer {
            background: #fff;
            text-align: center;
            padding: 20px;
            color: #777;
            font-size: 14px;
            border-top: 1px solid #eee;
        }

        @media (max-width: 600px) {
            .navbar {
                padding: 18px 5%;
                flex-direction: column;
                gap: 14px;
            }

            .nav-links a {
                margin: 0 8px;
            }

            .success-card {
                padding: 35px 20px;
            }

            h1 {
                font-size: 24px;
            }

            .btn {
                display: block;
            }
        }
    </style>
</head>

<body>

    <nav class="navbar">
        <a class="logo"
           href="${pageContext.request.contextPath}/HTML/a.jsp">
            <i class="fa-solid fa-droplet"></i> Drop4Life
        </a>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/HTML/a.jsp">
                <i class="fa-solid fa-house"></i> Dashboard
            </a>

            <a href="${pageContext.request.contextPath}/HTML/profile.html">
                <i class="fa-solid fa-user"></i> My Profile
            </a>
        </div>
    </nav>

    <main class="success-section">
        <div class="success-card">

            <div class="success-icon">
                <i class="fa-solid fa-circle-check"></i>
            </div>

            <h1>Blood Request Submitted Successfully!</h1>

            <p class="message">
                Your blood request has been recorded successfully.
                Click Donor Availability to find matching donors.
            </p>

        <a class="btn"
         href="${pageContext.request.contextPath}/HTML/donors.html?requestId=${param.requestId}">
           <i class="fa-solid fa-users"></i> Donor Availability
        </a>

            <a class="btn btn-secondary"
               href="${pageContext.request.contextPath}/HTML/a.jsp">
                <i class="fa-solid fa-house"></i>
                Back to Dashboard
            </a>

        </div>
    </main>

    <footer>
        &copy; 2026 Drop4Life. Every Drop Can Save a Life.
    </footer>

</body>
</html>