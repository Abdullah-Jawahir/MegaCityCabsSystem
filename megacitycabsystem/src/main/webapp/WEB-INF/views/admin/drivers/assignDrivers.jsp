<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assign Drivers - Megacity Cab</title>

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!-- Load the existing dashboard CSS -->
    <link rel="stylesheet" href="<c:url value='/css/dashboard.css'/>">
    <!-- Load the existing manage panel CSS -->
    <link rel="stylesheet" href="<c:url value='/css/managePanel.css'/>">
    <!-- Custom CSS for Assign Drivers -->
    <link rel="stylesheet" href="<c:url value='/css/assignDrivers.css'/>">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .error-message {
            background-color: #f8d7da; /* Light red background */
            color: #721c24;         /* Dark red text */
            border: 1px solid #f5c6cb; /* Light red border */
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            text-align: center;
            font-size: 14px;
            width: fit-content;
        }
    </style>
</head>
<body>
    <div class="admin-container">
        <%@ include file="/WEB-INF/views/components/sidebar.jsp" %>
        <main class="main-content">
            <%@ include file="/WEB-INF/views/components/header.jsp" %>
            <div class="dashboard-content">
                <h2 class="page-title"><i class="fas fa-user-plus"></i> Assign Drivers to Vehicles</h2>

                <!-- Error Message Display -->
                <c:if test="${not empty param.error}">
                    <div class="error-message" id="errorMessage">
                        <c:out value="${param.error}"/>
                    </div>
                </c:if>

                <!-- Assignment Form -->
                <div class="assign-container">
                    <form action="driver?action=assignDriverToVehicle" method="post" class="assign-form">
                        <div class="form-group">
                            <label for="driverSelect">Select Driver</label>
                            <select name="driverId" id="driverSelect" required>
                                <option value="" disabled selected>Choose a driver</option>
                                <c:forEach var="driver" items="${availableDrivers}">
                                    <option value="${driver.driverId}">${driver.user.name} - ${driver.licenseNumber}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="vehicleSelect">Select Vehicle</label>
                            <select name="vehicleId" id="vehicleSelect" required>
                                <option value="" disabled selected>Choose a vehicle</option>
                                <c:forEach var="vehicle" items="${availableVehicles}">
                                    <option value="${vehicle.vehicleId}">${vehicle.plateNumber} - ${vehicle.model}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="assign-btn"><i class="fas fa-check-circle"></i> Assign Driver</button>
                    </form>
                </div>

                <!-- Assigned Drivers Table -->
                <h3 class="sub-title">Assigned Drivers</h3>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Driver</th>
                            <th>License Number</th>
                            <th>Assigned Vehicle</th>
                            <th>Plate Number</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
					    <c:forEach var="assigned" items="${assignedDrivers}">
					        <tr>
					            <td>${assigned.driverName}</td>  <!-- Access driver name through wrapper -->
					            <td>${assigned.driverLicenseNumber}</td> <!-- Access license number through wrapper -->
					            <td>${assigned.model}</td>  <!-- Access vehicle model through wrapper -->
					            <td>${assigned.plateNumber}</td> <!-- Access plate number through wrapper -->
					            <td>
								    <a href="driver?action=unassignDriver&vehicleId=${assigned.vehicle.vehicleId}"
								       class="delete-action"
								       onclick="return confirm('Are you sure you want to remove this assignment?');">
								       <i class="fas fa-user-slash"></i> Unassign
								    </a>
								</td>
					        </tr>
					    </c:forEach>
					</tbody>
                </table>
            </div>
        </main>
    </div>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const errorMessageDiv = document.getElementById('errorMessage');

            if (errorMessageDiv) {
                setTimeout(function() {
                    errorMessageDiv.style.display = 'none';
                }, 5000); // 5000 milliseconds = 5 seconds
            }
        });
    </script>
</body>
</html>