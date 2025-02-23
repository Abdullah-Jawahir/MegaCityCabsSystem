<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Bookings - Megacity Cab</title>
    <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
    <script>
        // JavaScript functions to handle booking actions
        function viewBookingDetails(bookingId) {
            // Redirect or open a modal to view details of the booking
            window.location.href = 'viewBookingDetails?bookingId=' + bookingId;
        }

        function updateBookingStatus(bookingId) {
            // Redirect or open a modal to update booking status
            window.location.href = 'updateBookingDetails?bookingId=' + bookingId;
        }
    </script>
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1>Manage Bookings</h1>
            <a href="admin" class="back-link">Back to Dashboard</a>
        </header>
        
        <main class="admin-main">
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
							    <a href="booking?action=deleteBookingDetails&bookingId=${booking.bookingId}" 
							       onclick="return confirm('Are you sure you want to delete this booking?');">Delete</a>
							</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>
