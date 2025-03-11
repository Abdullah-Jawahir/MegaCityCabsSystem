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

<script>
	//Client-side form validation
	document.addEventListener('DOMContentLoaded', function() {
	  // Get forms
	  const customerForm = document.getElementById('customerForm');
	  const driverForm = document.getElementById('driverForm');
	  
	  // Input validation patterns
	  const validationPatterns = {
	    phone: /^\d{10}$/,
	    email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
	    password: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/,
	    nic: /^[0-9]{9}[vVxX]$|^[0-9]{12}$/,
	    username: /^[a-zA-Z0-9_]{4,20}$/,
	    license: /^[A-Z0-9]{7,9}$/
	  };
	
	  // Error messages
	  const errorMessages = {
	    phone: 'Please enter a valid 10-digit phone number',
	    email: 'Please enter a valid email address',
	    password: 'Password must be at least 8 characters with letters and numbers',
	    nic: 'Please enter a valid NIC number (9 digits + V/X or 12 digits)',
	    username: 'Username should be 4-20 characters (letters, numbers, underscores)',
	    license: 'Please enter a valid license number (7-9 alphanumeric characters)',
	    required: 'This field is required'
	  };
	
	  // Function to validate a single input field
	  function validateField(input, pattern, errorMsg) {
	    const value = input.value.trim();
	    const errorElement = document.createElement('div');
	    errorElement.className = 'validation-error';
	    errorElement.style.color = 'var(--error-color)';
	    errorElement.style.fontSize = '12px';
	    errorElement.style.marginTop = '4px';
	    
	    // Clear existing error message
	    const existingError = input.parentNode.querySelector('.validation-error');
	    if (existingError) {
	      input.parentNode.removeChild(existingError);
	    }
	    
	    // Check if field is empty when required
	    if (input.hasAttribute('required') && value === '') {
	      errorElement.textContent = errorMessages.required;
	      input.parentNode.appendChild(errorElement);
	      input.style.borderColor = 'var(--error-color)';
	      return false;
	    }
	    
	    // If field has a value, validate against pattern
	    if (value !== '' && pattern && !pattern.test(value)) {
	      errorElement.textContent = errorMsg;
	      input.parentNode.appendChild(errorElement);
	      input.style.borderColor = 'var(--error-color)';
	      return false;
	    }
	    
	    // Valid input
	    input.style.borderColor = 'var(--success-color)';
	    return true;
	  }
	
	  // Set up input restrictions for specific fields
	  function setInputRestrictions() {
	    // Phone numbers - limit to 10 digits
	    const phoneInputs = document.querySelectorAll('#customer-phone, #driver-phone');
	    phoneInputs.forEach(input => {
	      input.setAttribute('maxlength', '10');
	      input.addEventListener('input', function(e) {
	        this.value = this.value.replace(/[^0-9]/g, '');
	      });
	    });
	    
	    // NIC - limit based on format (9+1 or 12 digits)
	    const nicInputs = document.querySelectorAll('#customer-nic');
	    nicInputs.forEach(input => {
	      input.setAttribute('maxlength', '12');
	      input.addEventListener('input', function(e) {
	        if (this.value.length <= 9) {
	          // First 9 characters should be digits only
	          this.value = this.value.replace(/[^0-9]/g, '');
	        } else if (this.value.length === 10) {
	          // 10th character should be v, V, x, or X
	          const nonDigitPart = this.value.slice(9).replace(/[^vVxX]/g, '');
	          this.value = this.value.slice(0, 9) + nonDigitPart;
	        } else {
	          // For 12-digit format, all should be digits
	          this.value = this.value.replace(/[^0-9]/g, '');
	        }
	      });
	    });
	    
	    // License number - limit to 9 characters, uppercase alphanumeric
	    const licenseInputs = document.querySelectorAll('#driver-license');
	    licenseInputs.forEach(input => {
	      input.setAttribute('maxlength', '9');
	      input.addEventListener('input', function(e) {
	        this.value = this.value.replace(/[^A-Z0-9]/g, '').toUpperCase();
	      });
	    });
	  }
	
	  // Add validation to all inputs
	  function setupFormValidation(form) {
	    const inputs = form.querySelectorAll('input');
	    
	    inputs.forEach(input => {
	      // Add blur event listener
	      input.addEventListener('blur', function() {
	        let pattern = null;
	        let errorMsg = '';
	        
	        // Determine validation pattern based on input type or id
	        if (input.type === 'email') {
	          pattern = validationPatterns.email;
	          errorMsg = errorMessages.email;
	        } else if (input.id.includes('phone')) {
	          pattern = validationPatterns.phone;
	          errorMsg = errorMessages.phone;
	        } else if (input.id.includes('password')) {
	          pattern = validationPatterns.password;
	          errorMsg = errorMessages.password;
	        } else if (input.id.includes('nic')) {
	          pattern = validationPatterns.nic;
	          errorMsg = errorMessages.nic;
	        } else if (input.id.includes('username')) {
	          pattern = validationPatterns.username;
	          errorMsg = errorMessages.username;
	        } else if (input.id.includes('license')) {
	          pattern = validationPatterns.license;
	          errorMsg = errorMessages.license;
	        }
	        
	        validateField(input, pattern, errorMsg);
	      });
	      
	      // Add input event listener to clear error on typing
	      input.addEventListener('input', function() {
	        input.style.borderColor = 'var(--border-color)';
	        const existingError = input.parentNode.querySelector('.validation-error');
	        if (existingError) {
	          input.parentNode.removeChild(existingError);
	        }
	      });
	    });
	  }
	
	  // Apply input restrictions
	  setInputRestrictions();
	
	  // Set up validation on both forms
	  if (customerForm) setupFormValidation(customerForm);
	  if (driverForm) setupFormValidation(driverForm);
	
	  // Handle form submission
	  function handleSubmit(event, form) {
	    event.preventDefault();
	    
	    let isValid = true;
	    const inputs = form.querySelectorAll('input');
	    
	    // Validate all fields before submission
	    inputs.forEach(input => {
	      let pattern = null;
	      let errorMsg = '';
	      
	      if (input.type === 'email') {
	        pattern = validationPatterns.email;
	        errorMsg = errorMessages.email;
	      } else if (input.id.includes('phone')) {
	        pattern = validationPatterns.phone;
	        errorMsg = errorMessages.phone;
	      } else if (input.id.includes('password')) {
	        pattern = validationPatterns.password;
	        errorMsg = errorMessages.password;
	      } else if (input.id.includes('nic')) {
	        pattern = validationPatterns.nic;
	        errorMsg = errorMessages.nic;
	      } else if (input.id.includes('username')) {
	        pattern = validationPatterns.username;
	        errorMsg = errorMessages.username;
	      } else if (input.id.includes('license')) {
	        pattern = validationPatterns.license;
	        errorMsg = errorMessages.license;
	      }
	      
	      const fieldValid = validateField(input, pattern, errorMsg);
	      if (!fieldValid) isValid = false;
	    });
	    
	    // If all validations pass, submit the form
	    if (isValid) {
	      // Add loading state to button
	      const submitButton = form.querySelector('button[type="submit"]');
	      if (submitButton) {
	        submitButton.classList.add('loading');
	        submitButton.disabled = true;
	      }
	      form.submit();
	    } else {
	      // Scroll to the first error
	      const firstError = form.querySelector('.validation-error');
	      if (firstError) {
	        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
	      }
	    }
	  }
	
	  // Add submit event listeners to forms
	  if (customerForm) {
	    customerForm.addEventListener('submit', function(event) {
	      handleSubmit(event, customerForm);
	    });
	  }
	  
	  if (driverForm) {
	    driverForm.addEventListener('submit', function(event) {
	      handleSubmit(event, driverForm);
	    });
	  }
	
	  // Enhance error and success messages
	  function enhanceMessages() {
	    // Enhance error messages
	    const errorMessages = document.querySelectorAll('.error-message');
	    errorMessages.forEach(msg => {
	      msg.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg> ${msg.innerHTML}`;
	      
	      // Add slide-down animation
	      msg.style.animation = 'slideDown 0.3s ease-out';
	      
	      // Auto-hide after 5 seconds
	      setTimeout(() => {
	        msg.style.animation = 'fadeOut 0.5s ease-out forwards';
	      }, 5000);
	    });
	    
	    // Enhance success messages
	    const successMessages = document.querySelectorAll('.success-message');
	    successMessages.forEach(msg => {
	      msg.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg> ${msg.innerHTML}`;
	      
	      // Add slide-down animation
	      msg.style.animation = 'slideDown 0.3s ease-out';
	      
	      // Auto-hide after 5 seconds
	      setTimeout(() => {
	        msg.style.animation = 'fadeOut 0.5s ease-out forwards';
	      }, 5000);
	    });
	  }
	  
	  // Call the enhance function
	  enhanceMessages();
	});
	
	// Add animations to the page
	const styleEl = document.createElement('style');
	styleEl.textContent = `
	  @keyframes slideDown {
	    from { opacity: 0; transform: translateY(-10px); }
	    to { opacity: 1; transform: translateY(0); }
	  }
	  
	  @keyframes fadeOut {
	    from { opacity: 1; }
	    to { opacity: 0; height: 0; padding: 0; margin: 0; overflow: hidden; }
	  }
	  
	  .register-input:focus {
	    transition: border-color 0.3s, box-shadow 0.3s;
	  }
	  
	  .register-input.valid {
	    border-color: var(--success-color) !important;
	    background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 8 8'%3E%3Cpath fill='%232ECC71' d='M2.3 6.73L.6 4.53c-.4-1.04.46-1.4 1.1-.8l1.1 1.4 3.4-3.8c.6-.63 1.6-.27 1.2.7l-4 4.6c-.43.5-.8.4-1.1.1z'/%3E%3C/svg%3E");
	    background-repeat: no-repeat;
	    background-position: right 12px center;
	    background-size: 12px 12px;
	    padding-right: 36px;
	  }
	  
	  .error-message, .success-message {
	    display: flex;
	    align-items: center;
	    gap: 8px;
	    border-left: 4px solid;
	    padding-left: 12px !important;
	  }
	  
	  .error-message {
	    border-left-color: var(--error-color);
	  }
	  
	  .success-message {
	    border-left-color: var(--success-color);
	  }
	`;
	document.head.appendChild(styleEl);
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