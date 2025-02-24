<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Drivers - Megacity Cab</title>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!-- Load the same dashboard and manage panel CSS files -->
    <link rel="stylesheet" href="<c:url value='/css/dashboard.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/managePanel.css'/>">
    <!-- Add Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="admin-container">
    <%@ include file="/WEB-INF/views/components/sidebar.jsp" %>
        <main class="main-content">
        <%@ include file="/WEB-INF/views/components/header.jsp" %>
	        <div class="dashboard-content">
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
							    <a href="driver?action=deleteDriver&driverId=${driver.driverId}" class="delete-action"
							       onclick="return confirm('Are you sure you want to delete this driver?');">Delete</a>
							</td>
                        </tr>
                    </c:forEach>
	                </tbody>
	            </table>
	        </div>
		</main>
    </div>
    
     <script>
	   function showAddDriverForm() {
	       window.location.href = "<c:url value='/register?tab=driver'/>";
	   }
	</script>
</body>
</html>
