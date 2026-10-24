
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blood Request Submitted</title>
</head>
<body>

    <h2>${successMessage}</h2>
    <p>Your blood request has been saved successfully.</p>

    <a href="${pageContext.request.contextPath}/HTML/a.jsp">
        Back to Dashboard
    </a>

</body>
</html>
