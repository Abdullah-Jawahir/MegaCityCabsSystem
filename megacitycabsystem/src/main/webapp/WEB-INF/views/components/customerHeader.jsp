<%@ page import="com.system.model.User" %>
<header>
    <div class="header-top">
        <div class="logo">
            <img src="assets/images/megacitycab-logo.png" alt="Megacity Logo">
            <h1>Megacity Cab</h1>
        </div>
        <div class="nav-toggle" onclick="toggleNav()">
            <i class="fas fa-bars"></i> <!-- Hamburger Icon -->
        </div>
        <nav class="nav-menu">
            <ul>
                <li><a href="home"><i class="fas fa-taxi"></i> Home</a></li>
                <!-- <li><a href="booking/payment?action=viewPayments"><i class="fas fa-wallet"></i> Pay</a></li> -->
                <%
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                %>
                    <li><a href="booking?action=viewCustomerBookings"><i class="fas fa-history"></i> My Bookings</a></li>
                <% } %>
                <li><a href="support"><i class="fas fa-headset"></i> Help</a></li>
            </ul>
        </nav>
        <div class="user-actions">
            <%
                if (user != null) {
            %>
            <a href="customer?action=viewProfile" class="user-profile-icon">
                <i class="fas fa-user-circle fa-2x"></i>  <%-- Larger user icon --%>
                <span>Profile</span> <%-- Optional: Add "Profile" text --%>
            </a>
            <a href="logout" class="btn-secondary main-page-logout-in-btn"><i class="fas fa-sign-out-alt"></i> Logout</a>
            <% } else { %>
            <a href="login" class="btn-secondary main-page-sign-in-btn"><i class="fas fa-user"></i> Sign In</a>
            <a href="register" class="btn-primary">Register</a>
            <% } %>
        </div>
    </div>
</header>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Get the current URL
    const currentPath = window.location.pathname + window.location.search;

    // Get all navigation links
    const navLinks = document.querySelectorAll('.nav-menu ul li a');

    // Remove active class from all links first
    navLinks.forEach(link => link.classList.remove('active'));

    // Function to check if the URL contains specific keywords
    function containsKeyword(url, keyword) {
        return url.toLowerCase().includes(keyword.toLowerCase());
    }

    // Determine active link based on URL content
    if (currentPath.includes('/')) {
        if (containsKeyword(currentPath, 'home') || currentPath === '/megacitycabsystem/' || currentPath === '/megacitycabsystem') {
            document.querySelector('a[href*="home"]').classList.add('active');
        } else if (containsKeyword(currentPath, 'booking/payment')) {
            document.querySelector('a[href*="booking/payment"]').classList.add('active');
        }
         else if (containsKeyword(currentPath, 'booking')) {
            document.querySelector('a[href*="booking?action=viewCustomerBookings"]').classList.add('active');
        }
        else if (containsKeyword(currentPath, 'support')) {
            document.querySelector('a[href*="support"]').classList.add('active');
        }
    }
});
</script>