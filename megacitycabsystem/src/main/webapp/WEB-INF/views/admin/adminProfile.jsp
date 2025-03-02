<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Profile</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="<c:url value='/css/adminProfile.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/dashboard.css'/>">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="profile-container">
        <div class="profile-card">
            <div class="profile-header">
                <h2>Admin Profile</h2>
                <div class="profile-avatar">
                    <i class="fas fa-user-shield"></i>
                </div>
            </div>
            <div class="profile-body">
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-user profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Name</div>
                        <div class="profile-value">${adminUser.name}</div>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-id-badge profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Username</div>
                        <div class="profile-value">${adminUser.username}</div>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-icon-container">
                        <i class="fas fa-envelope profile-icon"></i>
                    </div>
                    <div class="profile-item-content">
                        <div class="profile-label">Email</div>
                        <div class="profile-value">${adminUser.email}</div>
                    </div>
                    </div>
                    <div class="profile-item">
                        <div class="profile-icon-container">
                            <i class="fas fa-phone profile-icon"></i>
                        </div>
                        <div class="profile-item-content">
                            <div class="profile-label">Phone</div>
                            <div class="profile-value">${adminUser.phone}</div>
                        </div>
                    </div>
                </div>
                 <div class="profile-footer">
                     <!--Moved button to new container with side each other-->
                   <a href="admin?action=dashboard" class="admin-btn back-link">Back to Dashboard</a>
                  <a href="admin?action=viewUpdateAdminSetting" class="admin-btn update-prof-btn">Update Profile</a>
               </div>
            </div>
        </div>
        </body>
        </html>