<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Booking - Megacity Cab</title>
    <link rel="stylesheet" href="<c:url value='/css/edit-forms.css'/>">
</head>
<body>
    <div class="form-container">
        <div class="form-card">
            <div class="form-header">
                <h1>Edit Booking</h1>
                <a href="booking?action=manageBookings" class="back-link">Back to Bookings</a>
            </div>

            <form action="booking" method="post">
                <input type="hidden" name="action" value="updateBookingDetails">
                <input type="hidden" name="bookingId" value="${booking.bookingId}">
                <!-- Hidden input for driver ID that will be updated via JavaScript -->
                <input type="hidden" name="driverId" id="driverId" value="${booking.assignedDriver.driverId}">

                <div class="form-content">
                    <div class="form-group">
                        <label for="pickupLocation">Pickup Location</label>
                        <input type="text" id="pickupLocation" name="pickupLocation" value="${booking.pickupLocation}" required>
                    </div>

                    <div class="form-group">
                        <label for="destination">Destination</label>
                        <input type="text" id="destination" name="destination" value="${booking.destination}" required>
                    </div>

                    <div class="form-group">
                        <label for="distance">Distance (km)</label>
                        <input type="text" 
                               id="distance" 
                               name="distance" 
                               value="${booking.distance}"
                               pattern="^\d*\.?\d*$"
                               onkeypress="return isNumberKey(event)"
                               onpaste="return validatePaste(event)"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <select id="status" name="status" required>
                            <option value="pending" ${booking.status == 'pending' ? 'selected' : ''}>Pending</option>
                            <option value="assigned" ${booking.status == 'assigned' ? 'selected' : ''}>Assigned</option>
                            <option value="in-progress" ${booking.status == 'in-progress' ? 'selected' : ''}>In Progress</option>
                            <option value="completed" ${booking.status == 'completed' ? 'selected' : ''}>Completed</option>
                            <option value="cancelled" ${booking.status == 'cancelled' ? 'selected' : ''}>Cancelled</option>
                        </select>
                    </div>

                    <div class="form-group">
					    <label for="vehicleId">Booked Vehicle</label>
					    <select id="vehicleId" name="vehicleId" onchange="updateDriverInfo()">
					        <c:forEach var="vehicle" items="${availableVehicles}">
					            <option value="${vehicle.vehicleId}" 
					                    data-driver-id="${vehicle.assignedDriver.driverId}"
					                    data-driver-name="${vehicle.assignedDriver.user.name}"
					                    ${booking.assignedVehicle.vehicleId == vehicle.vehicleId ? 'selected' : ''}>
					                ${vehicle.model}
					            </option>
					        </c:forEach>
					    </select>
					</div>

                    <div class="form-group">
                        <label>Assigned Driver</label>
                        <input type="text" id="driverDisplay" readonly 
                               value="${vehicle.assignedDriver.user.name}"
                               class="readonly-input">
                    </div>

                    <div class="form-group">
                        <label for="customerId">Customer</label>
                        <select id="customerId" name="customerId">
                            <c:forEach var="customer" items="${customers}">
                                <option value="${customer.customerId}" ${booking.customer.customerId == customer.customerId ? 'selected' : ''}>${customer.user.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn btn-secondary" onclick="history.back()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Update Booking</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        function isNumberKey(evt) {
            const charCode = (evt.which) ? evt.which : evt.keyCode;
            if (charCode === 46 && evt.target.value.includes('.')) {
                return false;
            }
            if (charCode !== 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
                return false;
            }
            return true;
        }

        function validatePaste(event) {
            const clipboardData = event.clipboardData || window.clipboardData;
            const pastedData = clipboardData.getData('Text');
            const regex = /^\d*\.?\d*$/;
            return regex.test(pastedData);
        }

        function updateDriverInfo() {
            const vehicleSelect = document.getElementById('vehicleId');
            const selectedOption = vehicleSelect.options[vehicleSelect.selectedIndex];
            const driverId = selectedOption.getAttribute('data-driver-id');
            const driverName = selectedOption.getAttribute('data-driver-name');
            
            document.getElementById('driverId').value = driverId;
            document.getElementById('driverDisplay').value = driverName;
        }

        // Initialize driver info on page load
        document.addEventListener('DOMContentLoaded', function() {
            updateDriverInfo();
        });
    </script>

    <style>
        .readonly-input {
            background-color: #f8f9fa;
            cursor: not-allowed;
        }
    </style>
</body>
</html>