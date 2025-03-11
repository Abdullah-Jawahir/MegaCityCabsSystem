<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Success - Megacity Cab</title>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="<c:url value='/css/paymentSuccess.css'/>">
</head>
<body>

<div class="payment-success-container">
    <div class="decoration decoration-1"></div>
    <div class="decoration decoration-2"></div>
    
    <div class="content">
        <div class="header">
            <h1>Payment Successful</h1>
        </div>
        
        <div class="success-checkmark">
            <div class="check-icon">
                <span class="icon-line line-tip"></span>
                <span class="icon-line line-long"></span>
                <div class="icon-circle"></div>
                <div class="icon-fix"></div>
            </div>
        </div>
        
        <div class="booking-id">
            <span class="booking-id-label">Your Booking ID</span>
            <span class="booking-id-value"><%= request.getParameter("bookingId") %></span>
        </div>
        
       	<div class="success-message">
		    <p>Your payment has been successfully processed, and your ride has been booked and confirmed.</p>
		</div>
		
		<div class="details">
		    <p>A confirmation email has been sent to your registered email address.</p>
		</div>
        
        <a href="<c:url value='home'/>" class="button">Back to Home</a>
    </div>
</div>

</body>
</html>
