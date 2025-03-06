<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Profile</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="<c:url value='/css/profile.css'/>">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="profile-container">
    <div class="profile-card">
        <div class="profile-header">
            <h2>My Details</h2>
            <div class="profile-avatar">
                <i class="fas fa-user"></i>
            </div>
        </div>
        <div class="profile-body">

            <c:if test="${not empty error}">
                <p style="color: red;">Error: ${error}</p>
            </c:if>

            <c:if test="${not empty user}">
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-user profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Name</div>
                        <div class="profile-value">${user.name}</div>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-user-circle profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Username</div>
                        <div class="profile-value">${user.username}</div>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-envelope profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Email</div>
                        <div class="profile-value">${user.email}</div>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-phone profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Phone</div>
                        <div class="profile-value">${user.phone}</div>
                    </div>
                </div>
            </c:if>
                <c:if test="${not empty customer}">
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-id-card profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Registration Number</div>
                        <div class="profile-value">${customer.registrationNumber}</div>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-map-marker-alt profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Address</div>
                        <div class="profile-value">${customer.address}</div>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-address-card profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">NIC</div>
                        <div class="profile-value">${customer.nic}</div>
                    </div>
                </div>
            </c:if>
                

        </div>
        <div class="profile-footer">
        	<a href="customer?action=viewUpdateCustomer" class="profile-btn update-prof-btn">Update Details</a>
            <a href="home" class="profile-btn back-link">Back to Home</a>
        </div>
    </div>
</div>
</body>
</html>