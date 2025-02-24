<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Driver - Megacity Cab</title>
    
    <link rel="stylesheet" href="<c:url value='/css/edit-forms.css'/>">
</head>
<body>
    <div class="form-container">
        <div class="form-card">
            <div class="form-header">
                <h1>Edit Driver</h1>
                <a href="driver?action=manageDrivers" class="back-link">Back to Drivers</a>
            </div>

            <form action="driver" method="post">
                <input type="hidden" name="action" value="updateDriver">
                <input type="hidden" name="driverId" value="${driver.driverId}">

                <div class="form-content">
                    <div class="form-group">
                        <label for="name">Full Name</label>
                        <input type="text" name="name" id="name" value="${driver.user.name}" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" name="email" id="email" value="${driver.user.email}" required>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone Number</label>
                        <input type="tel" name="phone" id="phone" value="${driver.user.phone}" required>
                    </div>

                    <div class="form-group">
                        <label for="licenseNumber">License Number</label>
                        <input type="text" name="licenseNumber" id="licenseNumber" value="${driver.licenseNumber}" required>
                    </div>

                    <div class="form-group">
					    <label for="status">Driver Status</label>
					    <select name="status" id="status" required>
					        <option value="Available" ${driver.status == 'Available' ? 'selected' : ''}>Available</option>
					        <option value="Busy" ${driver.status == 'Busy' ? 'selected' : ''}>Busy</option>
					        <option value="Inactive" ${driver.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
					    </select>
					</div>

                </div>

                <div class="form-actions">
                    <button type="button" class="btn btn-secondary" onclick="history.back()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Update Driver</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>