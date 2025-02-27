<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register - Megacity Cab</title>

  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<link rel="stylesheet" href="<c:url value='/css/register.css'/>" >
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
  
  <script>
	 function showTab(tabName) {
	   // Hide both forms
	   document.getElementById('customerForm').classList.add('hidden');
	   document.getElementById('driverForm').classList.add('hidden');
	   // Remove active class from tabs
	   document.getElementById('customerTab').classList.remove('active');
	   document.getElementById('driverTab').classList.remove('active');
	   // Show selected form and set active tab
	   if (tabName === 'customer') {
	     document.getElementById('customerForm').classList.remove('hidden');
	     document.getElementById('customerTab').classList.add('active');
	   } else if (tabName === 'driver') {
	     document.getElementById('driverForm').classList.remove('hidden');
	     document.getElementById('driverTab').classList.add('active');
	   }
	 }
	
	 window.onload = function() {
	   // Get URL parameters
	   const urlParams = new URLSearchParams(window.location.search);
	   const selectedTab = urlParams.get('tab') || 'customer'; 
	   
	   // Show the selected tab
	   showTab(selectedTab);
	 };
</script>


</head>
<body class="register-page">
  <div class="register-container">
    <div class="brand-header">
      <h2 class="register-title">Join Megacity Cab</h2>
      <p class="register-subtitle">Start your journey with us today</p>
    </div>
    
    <% if(request.getAttribute("error") != null) { %>
    <div class="error-message" style="color: red; margin: 10px 0; padding: 10px; background-color: #ffebee; border-radius: 4px;">
        <%= request.getAttribute("error") %>
    </div>
	<% } %>
	<% if(request.getAttribute("success") != null) { %>
	    <div class="success-message" style="color: green; margin: 10px 0; padding: 10px; background-color: #e8f5e9; border-radius: 4px;">
	        <%= request.getAttribute("success") %>
	    </div>
	<% } %>
	    
    <div class="tab-container">
      <div id="customerTab" class="tab" onclick="showTab('customer')">Customer</div>
      <div id="driverTab" class="tab" onclick="showTab('driver')">Driver</div>
    </div>
    
    <div class="form-container">
      <!-- Customer Registration Form -->
      <form id="customerForm" action="customer?action=registerCustomer" method="post">
        <input type="hidden" name="action" value="registerCustomer">
        <div class="register-form-group">
          <label for="customer-name" class="register-label">Full Name</label>
          <input type="text" id="customer-name" name="name" class="register-input" 
		    value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>"
		    placeholder="Enter your full name" required>
        </div>
         <div class="register-form-group">
          <label for="customer-username" class="register-label">Username</label>
          <input type="text" id="customer-username" name="username" class="register-input"
		    value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
		    placeholder="Enter your username" required>
        </div>
        <div class="register-form-group">
          <label for="customer-password" class="register-label">Password</label>
          <input type="password" id="customer-password" name="password" class="register-input" placeholder="Create a password" required>
        </div>
        <div class="register-form-group">
          <label for="customer-email" class="register-label">Email</label>
          <input type="email" id="customer-email" name="email" class="register-input" 
		    value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
		    placeholder="Enter your email" required>
        </div>
        <div class="register-form-group">
          <label for="customer-phone" class="register-label">Phone</label>
          <input type="text" id="customer-phone" name="phone" class="register-input" 
    		placeholder="Enter your phone number (10 digits)" required>
        </div>
        <div class="register-form-group">
          <label for="customer-nic" class="register-label">NIC</label>
         <input type="text" id="customer-nic" name="nic" class="register-input" 
		    value="<%= request.getAttribute("nic") != null ? request.getAttribute("nic") : "" %>"
		    placeholder="Enter your NIC" required>
        </div>
        <div class="register-form-group">
          <label for="customer-address" class="register-label">Address</label>
          <input type="text" id="customer-address" name="address" class="register-input" 
		    value="<%= request.getAttribute("address") != null ? request.getAttribute("address") : "" %>"
		    placeholder="Enter your address" required>
        </div>
        <button type="submit" class="register-btn">Create Customer Account</button>
      </form>
      
      <!-- Driver Registration Form -->
      <form id="driverForm" action="driver?action=registerDriver" method="post" class="hidden">
        <input type="hidden" name="action" value="registerDriver">
        <div class="register-form-group">
          <label for="driver-name" class="register-label">Full Name</label>
          <input type="text" id="driver-name" name="name" class="register-input" 
		    value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>"
		    placeholder="Enter your full name" required>
        </div>
        <div class="register-form-group">
          <label for="driver-username" class="register-label">Username</label>
          <input type="text" id="driver-username" name="username" class="register-input"
		    value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
		    placeholder="Enter your username" required>
        </div>
        <div class="register-form-group">
          <label for="driver-password" class="register-label">Password</label>
          <input type="password" id="driver-password" name="password" class="register-input" placeholder="Create a password" required>
        </div>
        <div class="register-form-group">
          <label for="driver-email" class="register-label">Email</label>
          <input type="email" id="driver-email" name="email" class="register-input" 
		    value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
		    placeholder="Enter your email" required>
        </div>
        <div class="register-form-group">
          <label for="driver-phone" class="register-label">Phone</label>
          <input type="text" id="driver-phone" name="phone" class="register-input" 
    		placeholder="Enter your phone number (10 digits)" required>
        </div>
        <div class="register-form-group">
          <label for="driver-license" class="register-label">License Number</label>
          <input type="text" id="driver-license" name="licenseNumber" class="register-input" 
		    value="<%= request.getAttribute("licenseNumber") != null ? request.getAttribute("licenseNumber") : "" %>" 
		    placeholder="Enter your license number" required>
        </div>
        <button type="submit" class="register-btn">Create Driver Account</button>
      </form>
    </div>
    
    <p class="register-text">Already have an account? <a href="login" class="register-link">Sign in</a></p>
  </div>
</body>
</html>