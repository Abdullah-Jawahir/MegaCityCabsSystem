<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Vehicle - Megacity Cab</title>
    
    <link rel="stylesheet" href="<c:url value='/css/edit-forms.css'/>">
</head>
<body>
	<div class="form-container">
	   <div class="form-card">
	        <div class="form-header">
	            <h1>Edit Vehicle</h1>
	            <a href="vehicle?action=manageVehicles" class="back-link">Back to Vehicles</a>
	        </div>
	        <div class="form-content">
	            <form action="vehicle" method="post">
	                <input type="hidden" name="action" value="updateVehicle">
	                <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}">
	                
	                <div class="form-group">
	                    <label for="plateNumber">Plate Number</label>
	                    <input type="text" id="plateNumber" name="plateNumber" value="${vehicle.plateNumber}" placeholder="Enter license plate" required>
	                </div>
	                
	                <div class="form-group">
	                    <label for="model">Model</label>
	                    <input type="text" id="model" name="model" value="${vehicle.model}" placeholder="Enter vehicle model" required>
	                </div>
	                
	                <div class="form-group">
                        <label for="ratePerKm">Rate Per KM ($)</label>
                        <input type="text" 
                               id="ratePerKm" 
                               name="ratePerKm"
                               value="${vehicle.ratePerKm}"
                               placeholder="Enter the rate"
                               pattern="^\d*\.?\d*$"
                               onkeypress="return isNumberKey(event)"
                               onpaste="return validatePaste(event)"
                               required>
                	</div>
	                
	                <div class="form-group">
					   <label for="status">Status</label>
					   <select id="status" name="status" required>
					       <option value="Active" ${vehicle.status == 'Active' ? 'selected="selected"' : ''}>Active</option>
					       <option value="Under Maintenance" ${vehicle.status == 'Under-Maintenance' ? 'selected="selected"' : ''}>Under Maintenance</option>
					       <option value="Retired" ${vehicle.status == 'Retired' ? 'selected="selected"' : ''}>Retired</option>
					    </select>
					</div>

	                
	                <div class="form-group">
					    <label for="driverId">Assign Driver</label>
					    <select id="driverId" name="driverId">
					        <c:forEach var="driver" items="${availableDrivers}">
					            <option value="${driver.driverId}" ${driver.driverId == vehicle.driverId ? 'selected="selected"' : ''}>
					                ${driver.user.name}
					            </option>
					        </c:forEach>
					    </select>
					</div>

	                
	                <div class="form-actions">
	                	<button type="button" class="btn btn-secondary" onclick="history.back()">Cancel</button>
	                    <button type="submit" class="btn btn-primary">Update Vehicle</button>
	                </div>
	            </form>
	        </div>
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
    </script>
</body>
</html>