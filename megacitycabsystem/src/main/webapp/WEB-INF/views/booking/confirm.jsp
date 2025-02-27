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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
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
                <div class="check-icon-wrapper">
                    <i class="fas fa-check-circle check-icon"></i>
                </div>
                <h1>Confirm the Booking</h1>
                <p>Booking ID: <%= booking.getBookingId() %></p>
            </div>
            
            <div class="booking-content">
                <div class="trip-summary">
                    <div class="location-box">
                        <div class="location-icon pickup-icon">
                            <i class="fas fa-map-marker-alt"></i>
                        </div>
                        <div class="location-title">Pickup Location</div>
                        <div class="location-address"><%= booking.getPickupLocation() %></div>
                    </div>
                    
                    <div class="location-arrow">
                        <i class="fas fa-arrow-right fa-2x"></i>
                    </div>
                    
                    <div class="location-box">
                        <div class="location-icon dropoff-icon">
                            <i class="fas fa-location-arrow"></i>
                        </div>
                        <div class="location-title">Drop Location</div>
                        <div class="location-address"><%= booking.getDestination() %></div>
                    </div>
                </div>
                
                <div class="details-grid">
                    <div class="detail-box">
                        <div class="detail-icon">
                            <i class="fas fa-road"></i>
                        </div>
                        <div class="detail-content">
                            <div class="detail-title">Distance</div>
                            <div class="detail-value"><%= booking.getDistance() %> km</div>
                        </div>
                    </div>
                    
                    <div class="detail-box">
                        <div class="detail-icon">
                            <i class="fas fa-clock"></i>
                        </div>
                        <div class="detail-content">
                            <div class="detail-title">Booking Time</div>
                            <div class="detail-value"><%= formattedDateTime %></div>
                        </div>
                    </div>
                    
                    <div class="detail-box">
                        <div class="detail-icon">
                            <i class="fas fa-user"></i>
                        </div>
                        <div class="detail-content">
                            <div class="detail-title">Driver</div>
                            <div class="detail-value"><%= booking.getAssignedDriver().getUser().getName() %></div>
                            <div class="detail-sub"><%= booking.getAssignedDriver().getUser().getPhone() %></div>
                        </div>
                    </div>
                    
                    <div class="detail-box">
                        <div class="detail-icon">
                            <i class="fas fa-car"></i>
                        </div>
                        <div class="detail-content">
                            <div class="detail-title">Vehicle</div>
                            <div class="detail-value"><%= booking.getAssignedVehicle().getModel() %></div>
                            <div class="detail-sub"><%= booking.getAssignedVehicle().getPlateNumber() %></div>
                        </div>
                    </div>
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
                    
                    <div class="action-buttons">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-credit-card"></i> Proceed to Payment
                        </button>
                        <button type="button" class="btn btn-danger" onclick="cancelBooking()">
                            <i class="fas fa-times"></i> Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script>
        function cancelBooking() {
            if (confirm("Are you sure you want to cancel this booking?")) {
                window.location.href = "index.jsp";
            }
        }
    </script>
</body>
</html>