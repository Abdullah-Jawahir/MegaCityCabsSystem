<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Booking - Megacity Cab</title>

    <link rel="stylesheet" href="<c:url value='/css/dashboard.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/managePanel.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/edit-forms.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/booking-map.css'/>"> <!-- New custom CSS for map -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.min.css" />
	<script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.min.js"></script>
</head>
<body>
    <div class="admin-container">
        <%@ include file="/WEB-INF/views/components/sidebar.jsp" %>
        <main class="main-content">
            <%@ include file="/WEB-INF/views/components/header.jsp" %>
            <div class="dashboard-content">
                <div class="form-container">
                    <div class="form-card">
                        <div class="form-header">
                            <h1>Add New Booking</h1>
                        </div>
                        <form id="bookingForm" action="booking?action=processCreateBooking" method="post">
                            <div class="form-content">
                                <!-- Map section -->
                                <div class="map-section">
                                    <h3 class="sub-heading"><i class="fas fa-map-marker-alt"></i> Location Selection</h3>
                                    
                                    <!-- Location inputs with icons -->
                                    <div class="form-group location-input-container">
                                        <i class="fas fa-dot-circle location-icon"></i>
                                        <input type="text" id="pickupLocation" name="pickupLocation" required 
                                            class="form-input location-input" placeholder="Enter pickup location">
                                        <button type="button" id="useMyLocation" class="use-location-btn">
                                            <i class="fas fa-crosshairs"></i> Use my location
                                        </button>
                                    </div>
                                    
                                    <div class="location-input-container form-group">
                                        <i class="fas fa-map-pin location-icon"></i>
                                        <input type="text" id="dropLocation" name="destination" required 
                                            class="form-input location-input" placeholder="Enter destination">
                                    </div>
                                    
                                    <!-- Map container -->
                                    <div class="map-container">
                                        <div id="map"></div>
                                        <div class="map-controls">
                                            <button type="button" id="zoomIn" class="map-control-btn" title="Zoom In">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                            <button type="button" id="zoomOut" class="map-control-btn" title="Zoom Out">
                                                <i class="fas fa-minus"></i>
                                            </button>
                                            <button type="button" id="centerMap" class="map-control-btn" title="Center Map">
                                                <i class="fas fa-compress-arrows-alt"></i>
                                            </button>
                                        </div>
                                    </div>
                                    
                                    <!-- Distance display -->
                                    <div class="distance-container">
                                        <i class="fas fa-route distance-icon"></i>
                                        <div class="distance-info">
                                            Estimated distance: <span id="distanceValue">0.0</span> km
                                        </div>
                                        <!-- Hidden field to store distance value -->
                                        <input type="hidden" id="distanceField" name="distance" value="0">
                                    </div>
                                </div>

                                <!-- Booking details section -->
                                <div class="booking-details-section">
                                    <h3 class="sub-heading"><i class="fas fa-clipboard-list"></i> Booking Details</h3>
                                    
                                    <div class="form-group">
                                        <label for="status">Status</label>
                                        <select id="status" name="status" required class="form-input">
                                            <option value="pending">Pending</option>
                                            <option value="assigned">Assigned</option>
                                            <option value="in-progress">In-Progress</option>
                                            <option value="completed">Completed</option>
                                            <option value="cancelled">Cancelled</option>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label for="customerId">Customer</label>
                                        <select id="customerId" name="customerId" required class="form-input">
                                            <c:forEach var="customer" items="${customers}">
                                                <option value="${customer.customerId}">${customer.user.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label for="vehicleId">Vehicle</label>
                                        <select id="vehicleId" name="vehicleId" onchange="updateDriverInfo()" required class="form-input">
                                            <option value="">Select a Vehicle</option>
                                            <c:forEach var="vehicle" items="${availableVehicles}">
                                                <option value="${vehicle.vehicleId}"
                                                        data-driver-id="${vehicle.assignedDriver.driverId}"
                                                        data-driver-name="${vehicle.assignedDriver.user.name}">
                                                    ${vehicle.model} - ${vehicle.plateNumber}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="driverId">Assigned Driver</label>
                                        <select id="driverId" name="driverId" required class="form-input">
                                            <option value="">Select a Vehicle First</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="form-actions">
                                <button type="button" class="btn btn-secondary" onclick="history.back()">Cancel</button>
                                <button type="submit" class="btn btn-primary">Create Booking</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <!-- Scripts -->
    <script>
        function updateDriverInfo() {
            const vehicleSelect = document.getElementById('vehicleId');
            const selectedOption = vehicleSelect.options[vehicleSelect.selectedIndex];
            const driverId = selectedOption.getAttribute('data-driver-id');
            const driverName = selectedOption.getAttribute('data-driver-name');

            const driverSelect = document.getElementById('driverId');
            driverSelect.innerHTML = "";
            
            if (driverId && driverName) {
                const newOption = document.createElement('option');
                newOption.value = driverId;
                newOption.text = driverName;
                driverSelect.add(newOption);
            } else {
                const newOption = document.createElement('option');
                newOption.value = "";
                newOption.text = "No driver assigned to this vehicle";
                driverSelect.add(newOption);
            }
        }

        document.addEventListener('DOMContentLoaded', function() {
            updateDriverInfo();
        });
    </script>
    
    <!-- Load the map functionality -->
    <script src="<c:url value='/js/addBookingMap.js'/>"></script>
</body>
</html>