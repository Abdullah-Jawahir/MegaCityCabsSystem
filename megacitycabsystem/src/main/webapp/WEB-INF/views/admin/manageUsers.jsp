<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - Megacity Cab</title>
    
	<link rel="stylesheet" href="<c:url value='/css/managePanel.css'/>" >
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1>Manage Users</h1>
            <a href="admin" class="back-link">Back to Dashboard</a>
        </header>
        
        <main class="admin-main">
            <div class="action-buttons">
                <button onclick="showAddUserForm()">Add New User</button>
            </div>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.userId}</td>
                            <td>${user.name}</td>
                            <td>${user.email}</td>
                            <td>${user.role}</td>
                            <td>
                                <button onclick="editUser(${user.userId})">Edit</button>
                                <button onclick="deleteUser(${user.userId})">Delete</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>