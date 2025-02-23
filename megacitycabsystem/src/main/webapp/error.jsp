<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <style>
        /* Simple internal CSS to style the error page */
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .error-container {
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 400px;
        }
        .error-container h1 {
            color: #d9534f;
        }
        .error-container p {
            color: #555;
            margin-top: 20px;
        }
        .error-container a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .error-container a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

    <div class="error-container">
        <h1>Error Occurred</h1>
        <p>
            <strong>Sorry, there was a problem processing your request.</strong>
        </p>

        <c:if test="${not empty errorMessage}">
            <p><strong>Error Message:</strong> ${errorMessage}</p>
        </c:if>

        <a href="<%= request.getContextPath() %>/index.jsp">Go Back to Home</a>
    </div>

</body>
</html>
