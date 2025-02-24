<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="sidebar">
    <div class="sidebar-header">
        <img src="<c:url value='/assets/images/megacitycab-logo.png'/>" alt="Logo" class="logo">
        <h2>Megacity Cab</h2>
    </div>
    <nav class="sidebar-nav">
        <a href="<c:url value='/admin'/>" class="nav-link">
            <i class="fas fa-home"></i>
            <span>Dashboard</span>
        </a>
        <a href="<c:url value='/admin?action=manageCustomers'/>" class="nav-link">
            <i class="fas fa-users"></i>
            <span>Customers</span>
        </a>
        <a href="<c:url value='/admin?action=manageBookings'/>" class="nav-link">
            <i class="fas fa-calendar"></i>
            <span>Bookings</span>
        </a>
        <a href="<c:url value='/admin?action=manageVehicles'/>" class="nav-link">
            <i class="fas fa-car"></i>
            <span>Vehicles</span>
        </a>
        <a href="<c:url value='/admin?action=manageDrivers'/>" class="nav-link">
            <i class="fas fa-id-card"></i>
            <span>Drivers</span>
        </a>
        <a href="<c:url value='/admin?action=reports'/>" class="nav-link">
            <i class="fas fa-chart-bar"></i>
            <span>Reports</span>
        </a>
    </nav>
</aside>

<!-- Add the navigation active state management script -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Get the current URL and query parameters
    const currentPath = window.location.pathname;
    const queryParams = new URLSearchParams(window.location.search);
    const currentAction = queryParams.get('action');
    
    // Get all navigation links
    const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
    
    // Remove active class from all links first
    navLinks.forEach(link => link.classList.remove('active'));
    
    // Handle active state based on current path and action
    switch(currentAction) {
        case 'manageCustomers':
            document.querySelector('a[href*="manageCustomers"]').classList.add('active');
            break;
        case 'manageBookings':
            document.querySelector('a[href*="manageBookings"]').classList.add('active');
            break;
        case 'manageVehicles':
            document.querySelector('a[href*="manageVehicles"]').classList.add('active');
            break;
        case 'manageDrivers':
            document.querySelector('a[href*="manageDrivers"]').classList.add('active');
            break;
        case 'reports':
            document.querySelector('a[href*="reports"]').classList.add('active');
            break;
        default:
            // If no action or on main admin page, highlight dashboard
            if (currentPath.includes('/admin') && !currentAction) {
                document.querySelector('a[href*="/admin"]').classList.add('active');
            }
            break;
    }
});
</script>