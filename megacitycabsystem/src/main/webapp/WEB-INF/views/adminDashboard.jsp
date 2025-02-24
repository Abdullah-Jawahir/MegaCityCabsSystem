<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Megacity Cab</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                        <select>
                            <option>Last 7 days</option>
                            <option>Last 30 days</option>
                            <option>This Month</option>
                            <option>This Year</option>
                        </select>
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
                                <span class="number">2,420</span>
                                <span class="trend positive">+12%</span>
                            </div>
                            <p class="stat-comparison">vs. previous month</p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: rgba(16, 185, 129, 0.1);">
                            <i class="fas fa-taxi" style="color: #10b981;"></i>
                        </div>
                        <div class="stat-details">
                            <h3>Active Bookings</h3>
                            <div class="stat-numbers">
                                <span class="number">140</span>
                                <span class="trend positive">+5%</span>
                            </div>
                            <p class="stat-comparison">vs. previous month</p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: rgba(245, 158, 11, 0.1);">
                            <i class="fas fa-car" style="color: #f59e0b;"></i>
                        </div>
                        <div class="stat-details">
                            <h3>Available Vehicles</h3>
                            <div class="stat-numbers">
                                <span class="number">15</span>
                                <span class="trend positive">+2</span>
                            </div>
                            <p class="stat-comparison">new this month</p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: rgba(139, 92, 246, 0.1);">
                            <i class="fas fa-dollar-sign" style="color: #8b5cf6;"></i>
                        </div>
                        <div class="stat-details">
                            <h3>Monthly Revenue</h3>
                            <div class="stat-numbers">
                                <span class="number">$28,000</span>
                                <span class="trend positive">+18%</span>
                            </div>
                            <p class="stat-comparison">vs. previous month</p>
                        </div>
                    </div>
                </div>

                <div class="dashboard-grid">
                    <div class="grid-item recent-bookings">
                        <h2>Recent Bookings</h2>
                        <div class="booking-list">
                            <div class="booking-item">
                                <div class="booking-info">
                                    <span class="booking-id">#12345</span>
                                    <span class="booking-customer">John Doe</span>
                                    <span class="booking-destination">Downtown</span>
                                </div>
                                <span class="booking-status pending">Pending</span>
                            </div>
                            <div class="booking-item">
                                <div class="booking-info">
                                    <span class="booking-id">#12344</span>
                                    <span class="booking-customer">Sarah Smith</span>
                                    <span class="booking-destination">Airport</span>
                                </div>
                                <span class="booking-status completed">Completed</span>
                            </div>
                            <div class="booking-item">
                                <div class="booking-info">
                                    <span class="booking-id">#12343</span>
                                    <span class="booking-customer">Mike Johnson</span>
                                    <span class="booking-destination">Mall</span>
                                </div>
                                <span class="booking-status active">Active</span>
                            </div>
                        </div>
                    </div>

                    <div class="grid-item top-drivers">
					    <h2>Top Performing Drivers</h2>
					    <div class="drivers-list">
					        <div class="driver-item">
					            <img src="<c:url value='/assets/images/taxi-driver-profile.png'/>" alt="Driver">
					            <div class="driver-info">
					                <h4>Alex Thompson</h4>
					                <p>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star-half-alt"></i> 4.9
					                </p>
					            </div>
					            <span class="driver-trips">126 trips</span>
					        </div>
					        <div class="driver-item">
					            <img src="<c:url value='/assets/images/taxi-driver-profile.png'/>" alt="Driver">
					            <div class="driver-info">
					                <h4>Maria Garcia</h4>
					                <p>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star-half-alt"></i> 4.8
					                </p>
					            </div>
					            <span class="driver-trips">118 trips</span>
					        </div>
					        <div class="driver-item">
					            <img src="<c:url value='/assets/images/taxi-driver-profile.png'/>" alt="Driver">
					            <div class="driver-info">
					                <h4>James Wilson</h4>
					                <p>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star"></i>
					                    <i class="fa-solid fa-star-half-alt"></i> 4.8
					                </p>
					            </div>
					            <span class="driver-trips">112 trips</span>
					        </div>
					    </div>
					</div>

                </div>
            </div>
            
            <div id="dynamic-content">
        	</div>
        </main>
    </div>
</body>
</html>