<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.system.model.Booking" %>
<%@ page import="com.system.model.Bill" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment - Megacity Cab</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="<c:url value='/css/payment.css'/>">
</head>
<body>

<%-- Retrieve the attributes from the request --%>
<%
    Booking booking = (Booking) request.getAttribute("booking");
    Bill bill = (Bill) request.getAttribute("bill");
    float baseAmount = (float) request.getAttribute("baseAmount");
    float taxAmount = (float) request.getAttribute("taxAmount");
    float totalAmount = (float) request.getAttribute("totalAmount");
%>

<div class="payment-container">
    <div class="payment-card">
        <h1>Payment for Booking ID: <%= booking != null ? booking.getBookingId() : "N/A" %></h1>
        <p>Bill ID: <%= bill != null ? bill.getBillId() : "N/A" %></p>

        <div class="bill-summary">
            <p>Base Fare: <strong>$<%= baseAmount %></strong></p>
            <p>Tax: <strong>$<%= taxAmount %></strong></p>
            <hr>
            <p>Total Amount: <strong>$<%= totalAmount %></strong></p>
        </div>

        <form action="/megacitycabsystem/billing" method="post">
            <input type="hidden" name="billId" value="<%= bill != null ? bill.getBillId() : "" %>">
            <input type="hidden" name="bookingId" value="<%= booking != null ? booking.getBookingId() : "" %>">
            <input type="hidden" name="baseAmount" value="<%= baseAmount %>">
            <input type="hidden" name="taxAmount" value="<%= taxAmount %>">
            <input type="hidden" name="totalAmount" value="<%= totalAmount %>">
            <input type="hidden" name="action" value="updateRideStatus"> <!-- New action parameter -->

            <label for="cardNumber">Card Number</label>
            <input type="text" id="cardNumber" name="cardNumber" required>

            <label for="expiry">Expiry Date</label>
            <input type="text" id="expiry" name="expiry" placeholder="MM/YY" required>

            <label for="cvv">CVV</label>
            <input type="text" id="cvv" name="cvv" required>

            <button type="submit" class="pay-btn">Pay Now</button>
        </form>
        
    </div>
</div>

</body>
</html>
