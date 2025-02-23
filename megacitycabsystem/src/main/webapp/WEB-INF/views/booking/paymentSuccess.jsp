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
    <h1>Payment Successful</h1>
    <div class="success-message">
        <p>Your booking ID is <strong><%= request.getParameter("bookingId") %></strong>.</p>
        <p>Your payment has been processed successfully, and your ride status is now "completed".</p>
    </div>
    
    <div class="tick-mark">
        <div class="checkmark">
            <div class="checkmark-circle"></div>
            <div class="checkmark-icon"></div>
        </div>
    </div>
</div>

</body>
</html>
