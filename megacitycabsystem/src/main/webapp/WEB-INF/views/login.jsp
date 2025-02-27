<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In - Megacity Cab</title>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<link rel="stylesheet" href="<c:url value='/css/signin.css'/>" >
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body class="signin-page">
    <div class="signin-container">
        <div class="brand-header">
            <h2 class="signin-title">Welcome Back</h2>
            <p class="signin-subtitle">Sign in to continue your journey</p>
        </div>

        <% if (session.getAttribute("errorMessage") != null) { %>
		    <p style="color: red;"><%= session.getAttribute("errorMessage") %></p>
		    <% session.removeAttribute("errorMessage"); %>
		<% } %>

        <form action="login" method="post" class="signin-form">

            <div class="signin-form-group">
			    <label for="username" class="signin-label">Username</label>
			    <input type="text" id="username" name="username" class="signin-input" placeholder="Enter your username" required>
			</div>

            <div class="signin-form-group">
                <label for="password" class="signin-label">Password</label>
                <input type="password" id="password" name="password" class="signin-input" placeholder="Enter your password" required>
            </div>

            <div class="forgot-password">
                <a href="#" class="forgot-password-link">Forgot password?</a>
            </div>

            <button type="submit" class="signin-btn">Sign In</button>
        </form>

        <p class="signin-text">Don't have an account? <a href="register" class="signin-link">Register</a></p>
    </div>
</body>
</html>