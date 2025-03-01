<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Vehicles - Megacity Cab</title>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!-- Make sure dashboard.css is loaded first -->
    <link rel="stylesheet" href="<c:url value='/css/dashboard.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/managePanel.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/addVehicleModal.css'/>">
    <!-- Add Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="admin-container">
        <%@ include file="/WEB-INF/views/components/sidebar.jsp" %>
        <main class="main-content">
        <%@ include file="/WEB-INF/views/components/header.jsp" %>
	        <div class="dashboard-content">
	            <div class="action-buttons">
	                <button onclick="showAddVehicleForm()">Add New Vehicle</button>
	            </div>
	            
	            <table class="data-table">
				    <thead>
				        <tr>
				            <th>Model</th>
				            <th>License Plate</th>
				            <th>Status</th>
				            <th>Driver</th>
				            <th>Rate Per KM</th>
				            <th>Actions</th>
				        </tr>
				    </thead>
				    <tbody>
			        <c:forEach var="vehicle" items="${vehicles}">
			            <tr>
			                <td>${vehicle.model}</td>
			                <td>${vehicle.plateNumber}</td>
			                <td class="status-${vehicle.status.toLowerCase()}">${vehicle.status}</td>
			                <td>
							    <c:choose>
							        <c:when test="${vehicle.driverId != 0}">
							            <c:set var="driverName" value="N/A" />
							            <c:forEach var="driver" items="${allDrivers}">
							                <c:if test="${driver.driverId == vehicle.driverId}">
							                    <c:set var="driverName" value="${driver.user.name}" />
							                </c:if>
							            </c:forEach>
							            ${driverName}
							        </c:when>
							        <c:otherwise>N/A</c:otherwise>
							    </c:choose>
							</td>
							<td>$${vehicle.ratePerKm}</td>
			                
			                <td>
			                    <a href="vehicle?action=editVehicle&vehicleId=${vehicle.vehicleId}">Edit</a>
			                    <a href="vehicle?action=deleteVehicle&vehicleId=${vehicle.vehicleId}" class="delete-action"
			                       onclick="return confirm('Are you sure you want to delete this vehicle?');">Delete</a>
			                </td>
			            </tr>
			        </c:forEach>
				    </tbody>
				</table>
				            
	        </div>
		</main>
    </div>

    <!-- Modal for Add Vehicle Form -->
    <div id="addVehicleModal" class="modal">
        <div class="modal-content">
            <h2>Add New Vehicle</h2>
            <form action="vehicle?action=createVehicle" method="post">
                <div class="form-group">
                    <label for="model">Vehicle Model</label>
                    <input type="text" id="model" name="model" placeholder="Enter vehicle model" required>
                </div>
                <div class="form-group">
                    <label for="plateNumber">License Plate</label>
                    <input type="text" id="plateNumber" name="plateNumber" placeholder="Enter license plate" required>
                </div>
                <div class="form-group">
                    <label for="driverId">Assign Available Driver</label>
                    <select id="driverId" name="driverId" required>
                        <option value="">-- Select a Driver --</option>
                        <c:forEach var="driver" items="${availableDrivers}">
                            <option value="${driver.driverId}">${driver.user.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                        <label for="ratePerKm">Rate Per KM</label>
                        <input type="text" 
                               id="ratePerKm" 
                               name="ratePerKm"
                               placeholder="Enter the rate"
                               pattern="^\d*\.?\d*$"
                               onkeypress="return isNumberKey(event)"
                               onpaste="return validatePaste(event)"
                               required>
                </div>
                <!-- You can add more fields if needed; for example, hidden field for default status -->
                <input type="hidden" name="status" value="Active">
                <div class="form-actions">
                    <button type="button" class="btn-secondary" onclick="hideAddVehicleForm()">Cancel</button>
                    <button type="submit" class="btn-primary">Add Vehicle</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        function showAddVehicleForm() {
            document.getElementById('addVehicleModal').style.display = 'block';
        }
        function hideAddVehicleForm() {
            document.getElementById('addVehicleModal').style.display = 'none';
        }
        // Hide modal if clicked outside of the modal content
        window.onclick = function(event) {
            var modal = document.getElementById('addVehicleModal');
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
        
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
    </script>
</body>
</html>
