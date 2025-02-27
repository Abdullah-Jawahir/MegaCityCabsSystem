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
    <link rel="stylesheet" href="<c:url value='/css/edit-forms.css'/>"> <!-- Use same CSS as edit page -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* Add or override styles for the form (if needed) */
    </style>
    <script>
        function updateDriverInfo() {
            const vehicleSelect = document.getElementById('vehicleId');
            const selectedOption = vehicleSelect.options[vehicleSelect.selectedIndex];
            const driverId = selectedOption.getAttribute('data-driver-id');
            const driverName = selectedOption.getAttribute('data-driver-name');

            const driverSelect = document.getElementById('driverId'); // Ensure the driver dropdown exists
            driverSelect.innerHTML = "";  // Clear existing options
            
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

        //call on page load
        document.addEventListener('DOMContentLoaded', function() {
            updateDriverInfo();
        });
    </script>
</head>
<body>
    <div class="admin-container">
        <%@ include file="/WEB-INF/views/components/sidebar.jsp" %>
        <main class="main-content">
            <%@ include file="/WEB-INF/views/components/header.jsp" %>
            <div class="dashboard-content">
                <div class="form-container"> <!-- Use same form-container as edit page -->
                    <div class="form-card"> <!-- Use same form-card as edit page -->
                        <div class="form-header"> <!-- Use same form-header as edit page -->
                            <h1>Add New Booking</h1>
                        </div>
                        <form action="booking?action=processCreateBooking" method="post">
                            <div class="form-content"> <!-- Use same form-content as edit page -->
                                <div class="form-group">
                                    <label for="pickupLocation">Pickup Location</label>
                                    <input type="text" id="pickupLocation" name="pickupLocation" required class="form-input">
                                </div>

                                <div class="form-group">
                                    <label for="destination">Destination</label>
                                    <input type="text" id="destination" name="destination" required class="form-input">
                                </div>

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
</body>
</html>