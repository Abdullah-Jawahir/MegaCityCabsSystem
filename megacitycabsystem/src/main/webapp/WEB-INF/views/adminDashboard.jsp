<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Megacity Cab</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<link rel="stylesheet" href="<c:url value='/css/admin.css'/>" >
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1>Admin Dashboard</h1>
            <nav class="admin-nav">
                <a href="admin?action=manageCustomers">Manage Customers</a>
                <a href="admin?action=manageBookings">Manage Bookings</a>
                <a href="admin?action=manageVehicles">Manage Vehicles</a>
                <a href="admin?action=manageDrivers">Manage Drivers</a>
            </nav>
        </header>
        
        <main class="admin-main">
            <div class="dashboard-cards">
                <div class="card">
                    <h3>Total Users</h3>
                    <!-- Add user count here -->
                </div>
                <div class="card">
                    <h3>Total Bookings</h3>
                    <!-- Add booking count here -->
                </div>
                <div class="card">
                    <h3>Total Vehicles</h3>
                    <!-- Add vehicle count here -->
                </div>
                <div class="card">
                    <h3>Total Drivers</h3>
                    <!-- Add driver count here -->
                </div>
            </div>
        </main>
    </div>
</body>
</html>