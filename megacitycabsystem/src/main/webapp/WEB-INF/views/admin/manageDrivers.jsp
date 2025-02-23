<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Drivers - Megacity Cab</title>
    
    <link rel="stylesheet" href="<c:url value='/css/admin.css'/>" >
    
    <script>
	   function showAddDriverForm() {
	       window.location.href = "<c:url value='/register?tab=driver'/>";
	   }
	</script>
    
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1>Manage Drivers</h1>
            <a href="admin" class="back-link">Back to Dashboard</a>
        </header>
        
        <main class="admin-main">
            <div class="action-buttons">
                <button onclick="showAddDriverForm()">Add New Driver</button>
            </div>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Driver ID</th>
                        <th>User Name</th> <!-- Updated to show userName -->
                        <th>Email</th> <!-- Added for better clarity -->
                        <th>License Number</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="driver" items="${drivers}">
                        <tr>
                            <td>${driver.driverId}</td>
                            <td>${driver.user.name}</td> <!-- Accessing user object for username -->
                            <td>${driver.user.email}</td> <!-- Accessing user object for email -->
                            <td>${driver.licenseNumber}</td>
                            <td>${driver.status}</td>
                            <td>
							    <a href="driver?action=editDriver&driverId=${driver.driverId}">Edit</a>
							    <a href="driver?action=deleteDriver&driverId=${driver.driverId}" 
							       onclick="return confirm('Are you sure you want to delete this driver?');">Delete</a>
							</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>
