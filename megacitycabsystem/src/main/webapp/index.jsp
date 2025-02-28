<%@ page import="com.system.model.User" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Megacity Cab System</title>
    <link rel="stylesheet" href="css/styles.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
    <!-- Add to the head section for Leaflet map support -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.min.css" />
	<script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.min.js"></script>
	<script type="text/javascript" src="js/locationMapper.js"></script>
	
	<script>
		function toggleNav() {
		    const navMenu = document.querySelector(".nav-menu");
		    navMenu.classList.toggle("active");
		}
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/views/components/customerHeader.jsp" %>

    <!-- Hero Section -->
	<section class="hero-section">
	    <div class="video-background">
	        <video autoplay muted loop>
	            <source src="assets/videos/cab-city-timelapse.mp4" type="video/mp4">   
	        </video>
	        <!-- Fallback image if video doesn't load -->
	         <img src="assets/images/hero-bg.avif" alt="City background">
	        <div class="overlay"></div>
	    </div>
	    
	    <div class="hero-content-wrapper">
	        <div class="user-details">
	            <% 
	                if (user != null) {
	            %>
	                <h2>Welcome, <%= user.getName() %></h2>
	            <% } %>
	        </div>
	        <h2 class="hero-title">Your Ride, Your Way</h2>
	        <p class="hero-subtitle">Experience comfortable and safe rides across the city</p>
	        <div class="booking-form-container">
	            <h3 class="booking-form-title">Book Your Ride</h3>
	            <form class="booking-form" action="processBooking" method="POST" id="bookingForm">
	                <input type="hidden" name="distance" id="distanceField" value="">
	                <div class="location-input-section">
		                <div class="input-field">
		                    <i class="fas fa-location-dot input-icon"></i>
		                    <input type="text" name="pickupLocation" id="pickupLocation" class="input-text" placeholder="Pickup Location" required>
		                    <button type="button" id="useMyLocation" class="location-btn">
		                        <i class="fas fa-crosshairs"></i>
		                    </button>
		                </div>
		                <div class="input-field">
		                    <i class="fas fa-location-arrow input-icon"></i>
		                    <input type="text" name="dropLocation" id="dropLocation" class="input-text" placeholder="Drop Location" required>
		                </div>
		            </div>
	                <div class="map-container">
	                    <div id="map"></div>
	                    <div id="distance-info">Estimated distance: <span id="distanceValue">0.0</span> km</div>
	                </div>
	                <button type="submit" class="booking-button">Book Now</button>
	            </form>
	        </div>
	    </div>
	</section>

    <!-- Features Section -->
    <section class="features">
        <h2>Why Choose Megacity</h2>
        <div class="features-grid">
            <div class="feature-card">
                <i class="fas fa-shield-alt"></i>
                <h3>Safe Rides</h3>
                <p>All our rides are monitored and drivers are verified</p>
            </div>
            <div class="feature-card">
                <i class="fas fa-clock"></i>
                <h3>24/7 Service</h3>
                <p>Available round the clock for your convenience</p>
            </div>
            <div class="feature-card">
                <i class="fas fa-tag"></i>
                <h3>Best Rates</h3>
                <p>Competitive pricing with no hidden charges</p>
            </div>
            <div class="feature-card">
                <i class="fas fa-star"></i>
                <h3>Top Rated</h3>
                <p>Highly rated drivers and premium service</p>
            </div>
        </div>
    </section>

    <!-- Download App Section -->
    <section class="download-app-section">
	    <div class="download-content">
	        <h2 class="download-title">Get Our Mobile App</h2>
	        <p class="download-subtitle">Download our app for a better experience</p>
	        <div class="download-buttons">
	            <a href="#" class="download-button google-play-button">
	                <i class="fab fa-google-play download-icon"></i> Google Play
	            </a>
	            <a href="#" class="download-button app-store-button">
	                <i class="fab fa-apple download-icon"></i> App Store
	            </a>
	        </div>
	    </div>
	    <div class="app-visual">
	        <img src="assets/images/megacitycab-app-logo.png" alt="Mobile App" class="app-logo">
	    </div>
	</section>

    <%@ include file="/WEB-INF/views/components/customerFooter.jsp" %>
</body>
</html>