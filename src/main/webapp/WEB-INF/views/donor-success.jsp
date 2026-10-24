
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donor Registration Successful | Drop4Life</title>

    <style>
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            font-family: Arial, sans-serif;
            background: #f7f8fa;
            text-align: center;
        }

        .success-card {
            max-width: 500px;
            margin: 20px;
            padding: 40px 30px;
            background: white;
            border-radius: 14px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
        }

        h1 {
            color: #198754;
        }

        p {
            color: #555;
            line-height: 1.7;
        }

        .btn {
            display: inline-block;
            margin: 10px 5px;
            padding: 12px 18px;
            border-radius: 7px;
            background: #d90429;
            color: white;
            text-decoration: none;
        }

        .secondary {
            background: #343a40;
        }
    </style>
</head>
<body>

    <div class="success-card">
        <h1>Registration Successful!</h1>

        <p>
            You are now registered as a blood donor.
            Thank you for helping save lives!
        </p>

        <p>
            Check Blood Requests to find requests matching
            your blood group and city.
        </p>

          <a class="btn"
            href="${pageContext.request.contextPath}/HTML/recipients.html">
                Check Blood Requests
            </a>

        <a class="btn secondary" href="${pageContext.request.contextPath}/HTML/a.jsp">
            Dashboard
        </a>
    </div>

</body>
</html>
