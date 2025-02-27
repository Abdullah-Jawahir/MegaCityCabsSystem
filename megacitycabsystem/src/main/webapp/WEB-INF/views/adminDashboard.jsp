<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Megacity Cab</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <link rel="stylesheet" href="<c:url value='/css/dashboard.css'/>" >
    <link rel="stylesheet" href="<c:url value='/css/adminDashboard.css'/>" >
    <!-- Add Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="admin-container">
        <%@ include file="components/sidebar.jsp" %>

        <main class="main-content">
            <%@ include file="components/header.jsp" %>

            <div class="dashboard-content">
                <div class="dashboard-header">
                    <h1>Dashboard Overview</h1>
                    <div class="date-filter">
                        <form action="admin" method="post" id="periodForm">
                            <input type="hidden" name="action" value="updateDashboardPeriod">
                            <select name="timePeriod" onchange="document.getElementById('periodForm').submit();">
                                <option value="last7days" ${selectedTimePeriod == 'last7days' ? 'selected' : ''}>Last 7 days</option>
                                <option value="last30days" ${selectedTimePeriod == 'last30days' ? 'selected' : ''}>Last 30 days</option>
                                <option value="thisMonth" ${selectedTimePeriod == 'thisMonth' ? 'selected' : ''}>This Month</option>
                                <option value="thisYear" ${selectedTimePeriod == 'thisYear' ? 'selected' : ''}>This Year</option>
                            </select>
                        </form>
                    </div>
                </div>

                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: rgba(37, 99, 235, 0.1);">
                            <i class="fas fa-users" style="color: #2563eb;"></i>
                        </div>
                        <div class="stat-details">
                            <h3>Total Customers</h3>
                            <div class="stat-numbers">
                                <span class="number">${totalCustomers}</span>
                                <span class="trend ${customerGrowth >= 0 ? 'positive' : 'negative'}">
                                    ${customerGrowth >= 0 ? '+' : ''}${customerGrowth}%
                                </span>
                            </div>
                            <p class="stat-comparison">vs. previous period</p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: rgba(16, 185, 129, 0.1);">
                            <i class="fas fa-taxi" style="color: #10b981;"></i>
                        </div>
                        <div class="stat-details">
                            <h3>Active Bookings</h3>
                            <div class="stat-numbers">
                                <span class="number">${activeBookings}</span>
                                <span class="trend ${bookingGrowth >= 0 ? 'positive' : 'negative'}">
                                    ${bookingGrowth >= 0 ? '+' : ''}${bookingGrowth}%
                                </span>
                            </div>
                            <p class="stat-comparison">vs. previous period</p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: rgba(245, 158, 11, 0.1);">
                            <i class="fas fa-car" style="color: #f59e0b;"></i>
                        </div>
                        <div class="stat-details">
                            <h3>Available Vehicles</h3>
                            <div class="stat-numbers">
                                <span class="number">${availableVehicles}</span>
                                <span class="trend positive">+${newVehicles}</span>
                            </div>
                            <p class="stat-comparison">new this period</p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: rgba(139, 92, 246, 0.1);">
                            <i class="fas fa-dollar-sign" style="color: #8b5cf6;"></i>
                        </div>
                        <div class="stat-details">
                            <h3>Period Revenue</h3>
                            <div class="stat-numbers">
                                <span class="number">$<fmt:formatNumber value="${monthlyRevenue}" pattern="#,##0.00"/></span>
                                <span class="trend ${revenueGrowth >= 0 ? 'positive' : 'negative'}">
                                    ${revenueGrowth >= 0 ? '+' : ''}${revenueGrowth}%
                                </span>
                            </div>
                            <p class="stat-comparison">vs. previous period</p>
                        </div>
                    </div>
                </div>

                <div class="dashboard-grid">
                    <div class="grid-item recent-bookings">
                        <h2>Recent Bookings</h2>
                        <div class="booking-list">
                            <c:forEach var="booking" items="${recentBookings}">
                                <div class="booking-item">
                                    <div class="booking-info">
                                        <span class="booking-id">#${booking.bookingId}</span>
                                        <span class="booking-customer">${booking.customer.user.name}</span>
                                        <span class="booking-destination">${booking.destination}</span>
                                    </div>
                                    <span class="booking-status ${booking.status.toLowerCase()}">${booking.status}</span>
                                </div>
                            </c:forEach>
                            
                            <c:if test="${empty recentBookings}">
                                <div class="no-data">No recent bookings found.</div>
                            </c:if>
                        </div>
                    </div>

					<div class="grid-item top-drivers">
					    <h2>Top Performing Drivers</h2>
					    <div class="drivers-list">
					        <c:forEach var="driver" items="${topDrivers}">
					            <div class="driver-item">
					                <img src="<c:url value='/assets/images/taxi-driver-profile.png'/>" alt="Driver">
					                <div class="driver-info">
					                    <h4>${driver.name}</h4>
					                    <p>
					                        <!-- Display full stars using the pre-calculated value -->
					                        <c:forEach var="i" begin="1" end="${driver.fullStars}">
					                            <i class="fa-solid fa-star"></i>
					                        </c:forEach>
					                        <!-- Display half star if needed -->
					                        <c:if test="${driver.avg_rating - driver.fullStars > 0}">
					                            <i class="fa-solid fa-star-half-alt"></i>
					                        </c:if>
					                        ${driver.avg_rating}
					                    </p>
					                </div>
					                <span class="driver-trips">${driver.total_bookings} trips</span>
					            </div>
					        </c:forEach>
					        
					        <c:if test="${empty topDrivers}">
					            <div class="no-data">No top drivers data available.</div>
					        </c:if>
					    </div>
					</div>
					                    
                </div>
            </div>
            
            <div id="dynamic-content">
            </div>
        </main>
    </div>

    <script>
        // Optional: Add JavaScript to handle AJAX updates or other dynamic functionality
        document.addEventListener('DOMContentLoaded', function() {
            // You can add code here to update dashboard without page reload
        });
    </script>
</body>
</html>