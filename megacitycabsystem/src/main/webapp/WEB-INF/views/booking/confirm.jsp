<%@ page import="com.system.model.Booking" %>
<%@ page import="com.system.model.Bill" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking Confirmation - Megacity Cab</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="<c:url value='/css/bookingConfirm.css'/>">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
</head>
<body>

<%
    Booking booking = (Booking) request.getAttribute("booking");
    Bill bill = (Bill) request.getAttribute("bill");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    String formattedDateTime = booking.getBookingTime().format(formatter);
%>

<div class="confirmation-container">
    <div class="confirmation-card">
        <div class="confirmation-header">
            <h1><i class="fas fa-check-circle"></i> Confirm the Booking</h1>
            <p class="booking-id">Booking ID: <%= booking.getBookingId() %></p>
        </div>

        <div class="booking-details">
            <div class="locations">
                <div class="location-item">
                    <i class="fas fa-map-marker-alt pickup-icon"></i>
                    <div class="location-text">
                        <h3>Pickup Location</h3>
                        <p><%= booking.getPickupLocation() %></p>
                    </div>
                </div>
                <div class="location-connector">
                    <i class="fa-solid fa-angles-right"></i>
                </div>
                <div class="location-item">
                    <i class="fas fa-location-arrow destination-icon"></i>
                    <div class="location-text">
                        <h3>Drop Location</h3>
                        <p><%= booking.getDestination() %></p>
                    </div>
                </div>
            </div>

            <div class="ride-details">
                <div class="detail-row">
                    <div class="detail-item">
                        <i class="fas fa-road detail-item-icon"></i>
                        <div class="detail-text">
                            <h3>Distance</h3>
                            <p><%= booking.getDistance() %> km</p>
                        </div>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-clock detail-item-icon"></i>
                        <div class="detail-text">
                            <h3>Booking Time</h3>
                            <p><%= formattedDateTime %></p>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="driver-vehicle-details">
                <div class="detail-row">
                    <div class="detail-item">
                        <i class="fas fa-user"></i>
                        <div class="detail-text">
                            <h3>Driver</h3>
                            <p><%= booking.getAssignedDriver().getUser().getName() %></p>
							<p class="sub-text"><%= booking.getAssignedDriver().getUser().getPhone() %></p>
                            
                        </div>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-car"></i>
                        <div class="detail-text">
                            <h3>Vehicle</h3>
                            <p><%= booking.getAssignedVehicle().getModel() %></p>
                            <p class="sub-text"><%= booking.getAssignedVehicle().getPlateNumber() %></p>
                        </div>
                    </div>
                </div>
            </div>

            <!--   
            <div class="bill-details">
                <h3>Bill Summary</h3>
                <p>Base Fare: $<%= bill.getBaseAmount() %></p>
                <p>Tax: $<%= bill.getTaxAmount() %></p>
                <hr>
                <p><strong>Total Amount: $<%= bill.getTotalAmount() %></strong></p>
            </div>
            -->
        </div>


        <form action="billing" method="post">
		    <input type="hidden" name="bookingId" value="<%= booking.getBookingId() %>">
		    <input type="hidden" name="pickupLocation" value="<%= booking.getPickupLocation() %>">
		    <input type="hidden" name="dropLocation" value="<%= booking.getDestination() %>">
		    <input type="hidden" name="distance" value="<%= booking.getDistance() %>">
		    <input type="hidden" name="baseAmount" value="<%= bill.getBaseAmount() %>">
		    <input type="hidden" name="taxAmount" value="<%= bill.getTaxAmount() %>">
		    <input type="hidden" name="totalAmount" value="<%= bill.getTotalAmount() %>">
		    <input type="hidden" name="vehicleId" value="<%= booking.getAssignedVehicle().getVehicleId() %>">
		    <input type="hidden" name="driverId" value="<%= booking.getAssignedDriver().getDriverId() %>">
		    <input type="hidden" name="customerId" value="<%= booking.getCustomer().getCustomerId() %>">
		    
		    <button type="submit" class="confirm-btn">Proceed to Payment</button>
		</form>



        <button class="cancel-btn" onclick="cancelBooking()">Cancel</button>
    </div>
</div>

<script>
    function cancelBooking() {
        window.location.href = "index.jsp";
    }
</script>
</body>
</html>
