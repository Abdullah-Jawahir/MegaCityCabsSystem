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

        <!-- Drivers Dropdown -->
        <button class="dropdown">
            <span class="main-context">
                <i class="fas fa-id-card"></i>
                Drivers
            </span>
            <i class="fas fa-chevron-down"></i>
        </button>
        <div class="dropdown-menu">
            <a href="<c:url value='/admin?action=manageDrivers'/>" class="nav-link">
                <i class="fas fa-user"></i>
                <span>Manage Drivers</span>
            </a>
            <a href="<c:url value='/admin?action=viewAssignDrivers'/>" class="nav-link">
                <i class="fas fa-user-plus"></i>
                <span>Assign Drivers</span>
            </a>
        </div>

        <a href="<c:url value='/admin?action=reports'/>" class="nav-link">
            <i class="fas fa-chart-bar"></i>
            <span>Reports</span>
        </a>
        <a href="<c:url value='/admin?action=updateBillSettings'/>" class="nav-link">
            <i class="fas fa-money-bill-wave"></i>
            <span>Bill Configurations</span>
        </a>
    </nav>
    
    <!-- Logout Button -->
    <a href="<c:url value='logout'/>" class="logout-button">
        <i class="fas fa-sign-out-alt"></i>
        <span>Logout</span>
    </a>
</aside>

<!-- JavaScript for dropdown toggle -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const dropdownButton = document.querySelector('.dropdown');
    const dropdownMenu = document.querySelector('.dropdown-menu');

    dropdownButton.addEventListener('click', function() {
        dropdownMenu.classList.toggle('show');
        dropdownButton.classList.toggle('active');
    });

    // Get current URL path
    const currentPath = window.location.pathname + window.location.search;

    // Get all navigation links
    const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');

    // Remove active class from all links first
    navLinks.forEach(link => link.classList.remove('active'));

    // Function to check if the URL contains specific keywords
    function containsKeyword(url, keyword) {
        return url.toLowerCase().includes(keyword.toLowerCase());
    }

    // Determine active link based on URL content
    if (currentPath.includes('/admin')) {
        if (containsKeyword(currentPath, 'manageCustomers')) {
            document.querySelector('a[href*="manageCustomers"]').classList.add('active');
        } else if (containsKeyword(currentPath, 'manageBookings')) {
            document.querySelector('a[href*="manageBookings"]').classList.add('active');
        } else if (containsKeyword(currentPath, 'manageVehicles')) {
            document.querySelector('a[href*="manageVehicles"]').classList.add('active');
        } else if (containsKeyword(currentPath, 'manageDrivers')) {
            document.querySelector('a[href*="manageDrivers"]').classList.add('active');
            dropdownMenu.classList.add('show');
            dropdownButton.classList.add('active');
        } else if (containsKeyword(currentPath, 'viewAssignDrivers')) {
            document.querySelector('a[href*="viewAssignDrivers"]').classList.add('active');
            dropdownMenu.classList.add('show');
            dropdownButton.classList.add('active');
        } else if (containsKeyword(currentPath, 'reports')) {
            document.querySelector('a[href*="reports"]').classList.add('active');
        } else if (containsKeyword(currentPath, 'updateBillSettings')) {
            document.querySelector('a[href*="updateBillSettings"]').classList.add('active');
        } else {
            // Default active dashboard
            document.querySelector('a[href*="/admin"]').classList.add('active');
        }
    }
});
</script>
