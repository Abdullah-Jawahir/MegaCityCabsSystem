<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Megacity Cab System - Invoice</title>

    <link rel="stylesheet" href="<c:url value='/css/bill.css'/>">
    <script>
	    window.onload = function() {
	        window.print();
	    };
    </script>
</head>
<body>
	<div id="bill-container">
	    <div class="bill-header">
	        <img src="<c:url value='assets/images/megacitycab-logo.png'/>" alt="Company Logo" class="logo">
	        <h1>Megacity Cab System</h1>
	        <p>123, City Road, Metropolis, USA</p>
	        <p>Email: support@megacitycabs.com | Phone: +1-234-567-890</p>
	        <hr>
	    </div>
	
	   <div class="bill-details">
		    <h2>Invoice</h2>
		    <p><b>Invoice No:</b> ${bill.billId}</p>
		    <p><b>Date:</b> 
		        <fmt:formatDate value="${date}" pattern="MMM dd, yyyy HH:mm"/>
		    </p>
		</div>
	
	    <div class="customer-details">
	        <h2>Customer Details</h2>
	        <p><b>Name:</b> ${booking.customer.user.name}</p>
	        <p><b>Contact:</b> ${booking.customer.user.phone}</p>
	    </div>
	
	    <div class="booking-details">
	        <h2>Ride Details</h2>
	        <p><b>Booking ID:</b> ${booking.bookingId}</p>
	        <p><b>Pickup Location:</b> ${booking.pickupLocation}</p>
	        <p><b>Destination:</b> ${booking.destination}</p>
	        <p><b>Distance:</b> ${booking.distance} km</p>
	        <p><b>Status:</b> ${booking.status}</p>
	    </div>
	
	    <div class="driver-details">
	        <h2>Driver Details</h2>
	        <p><b>Name:</b> ${booking.assignedDriver.user.name}</p>
	        <p><b>Vehicle:</b> ${booking.assignedVehicle.model} (${booking.assignedVehicle.plateNumber})</p>
	    </div>
	
	    <div class="payment-details">
	        <h2>Payment Summary</h2>
	        <p><b>Base Fare:</b> <fmt:formatNumber value="${bill.baseAmount}" type="currency"/></p>
	        <p><b>Tax:</b> <fmt:formatNumber value="${bill.taxAmount}" type="currency"/></p>
	        <p><b>Discount:</b> <fmt:formatNumber value="${bill.discountAmount}" type="currency"/></p>
	        <p><b>Total Amount:</b> <fmt:formatNumber value="${bill.totalAmount}" type="currency"/></p>
	        <%-- <p><b>Payment Method:</b> ${bill.paymentMethod}</p> --%>
	        <p><b>Status:</b> <span class="status ${bill.status}">${bill.status}</span></p>
	    </div>
	
	    <div class="footer">
	        <p>Thank you for choosing Megacity Cab System!</p>
	        <p>Have a great day!</p>
	    </div>
	</div>
</body>
</html>
