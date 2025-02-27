<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Bookings - Megacity Cab</title>
    
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
                    <button onclick="showAddBookingForm()">
                        <i class="fas fa-plus"></i> Add New Booking
                    </button>
                </div>
	            <table class="data-table">
	                <thead>
	                    <tr>
	                        <th>Customer</th>
	                        <th>Assigned Driver</th>
	                        <th>Booked Vehicle</th>
	                        <th>Booking Time</th>
	                        <th>Pick Up Location</th>
	                        <th>Destination</th>
	                        <th>Distance</th>
	                        <th>Status</th>
	                        <th>Actions</th>
	                    </tr>
	                </thead>
	                <tbody>
                    <c:forEach var="booking" items="${bookings}">
                        <tr>
                            <td>${booking.customer.user.name}</td>
                            <td>
							    <c:if test="${not empty booking.assignedDriver}">
							        ${booking.assignedDriver.user.name}
							    </c:if>
							</td>
                            <td>${booking.assignedVehicle.model}</td>
                            <td>${booking.bookingTime}</td>
                            <td>${booking.pickupLocation}</td>
                            <td>${booking.destination}</td>
                            <td>${booking.distance}</td>
                            <td>${booking.status}</td>
                            <td>
							    <a href="booking?action=editBookingDetails&bookingId=${booking.bookingId}">Edit</a>
							    <a href="booking?action=deleteBookingDetails&bookingId=${booking.bookingId}" class="delete-action"
							       onclick="return confirm('Are you sure you want to delete this booking?');">Delete</a>
							</td>
                        </tr>
                    </c:forEach>
	                </tbody>
	            </table>
	        </div>
     	</main>
    </div>
    
    <script>
	   function showAddBookingForm() {
		   window.location.href = "<c:url value='/booking?action=createBooking'/>";
	   }
	</script>
</body>
</html>
