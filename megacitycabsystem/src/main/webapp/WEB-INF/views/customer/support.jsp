<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Megacity Cab System - Help & Support</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/support.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />

</head>
<body>
	<%@ include file="/WEB-INF/views/components/customerHeader.jsp" %>

    <main class="support-page">
        <section class="hero">
            <h1>How can we help you?</h1>
            <p>Find answers to frequently asked questions and guides on using Megacity Cab System.</p>
        </section>

        <section class="help-categories">
            <h2>Browse by Category</h2>
            <div class="category-grid">
                <div class="category-card">
                    <i class="fas fa-book"></i>
                    <h3>Getting Started</h3>
                    <p>Learn the basics of using our platform.</p>
                    <a href="#getting-started" class="read-more">Read More</a>
                </div>

                <div class="category-card">
                    <i class="fas fa-taxi"></i>
                    <h3>Booking a Ride</h3>
                    <p>Step-by-step guide on booking your first ride.</p>
                    <a href="#booking-ride" class="read-more">Read More</a>
                </div>

                <div class="category-card">
                    <i class="fas fa-wallet"></i>
                    <h3>Payments & Billing</h3>
                    <p>Information about payment methods and managing your billing details.</p>
                    <a href="#payments" class="read-more">Read More</a>
                </div>

                 <div class="category-card">
                    <i class="fas fa-history"></i>
                    <h3>My Bookings</h3>
                    <p>How to view your booking history and manage upcoming rides.</p>
                    <a href="#my-bookings" class="read-more">Read More</a>
                </div>

                <div class="category-card">
                    <i class="fas fa-user"></i>
                    <h3>Account Management</h3>
                    <p>Managing your profile, settings, and security.</p>
                    <a href="#account" class="read-more">Read More</a>
                </div>

                <div class="category-card">
                    <i class="fas fa-question-circle"></i>
                    <h3>Frequently Asked Questions</h3>
                    <p>Answers to common questions about our service.</p>
                    <a href="#faq" class="read-more">Read More</a>
                </div>
            </div>
        </section>

        <section id="getting-started" class="help-section">
            <h2>Getting Started</h2>
            <p>Welcome to Megacity Cab!  This section will guide you through the initial steps of using our platform.</p>

            <h3>1. Creating an Account</h3>
            <p>To begin, you'll need to create an account. Follow these steps:</p>
            <ol>
                <li>Click on the "Register" button in the top right corner of the homepage.</li>
                <li>Fill in the required information, such as your name, email address, and password.</li>
                <li>Click the "Submit" button to complete the registration.</li>
            </ol>
            <p>After submitting, you may receive a confirmation email. Click the link in the email to activate your account.</p>

            <h3>2. Logging In</h3>
            <p>Once your account is activated, you can log in:</p>
            <ol>
                <li>Click on the "Sign In" button in the top right corner.</li>
                <li>Enter your registered email address and password.</li>
                <li>Click the "Login" button.</li>
            </ol>
        </section>

        <section id="booking-ride" class="help-section">
            <h2>Booking a Ride</h2>
            <p>This section explains how to book a ride using Megacity Cab.</p>

            <h3>1. Entering Pickup and Drop-off Locations</h3>
            <p>On the homepage, you'll find the booking form:</p>
            <ol>
                <li>Enter your desired pickup location in the "Pickup Location" field.</li>
                <li>Enter your desired drop-off location in the "Drop Location" field.</li>
            </ol>

            <h3>2. Viewing the Map and Distance</h3>
            <p>Our system will display a map showing the route between your pickup and drop-off locations, along with an estimated distance.</p>

            <h3>3. Confirming and Booking</h3>
            <p>Once you are satisfied with the locations, click the "Book Now" button.</p>
            <p>You'll be taken to a confirmation page where you can review your booking details.</p>
        </section>

         <section id="payments" class="help-section">
            <h2>Payments & Billing</h2>
            <p>This section explains how to manage your payment methods and billing details.</p>

            <h3>1. Adding Payment Methods</h3>
            <p>Go to payment page by selecting "Pay" at the header</p>
            <ol>
                <li>Click on the "add payment method" button</li>
                <li>Select payment method to link the account or add new cards</li>
            </ol>

            <h3>2. View balance and payment History</h3>
            <p>Once you are on the payment page, you can also view the balance and payment history of your account</p>
        </section>

        <section id="my-bookings" class="help-section">
            <h2>My Bookings</h2>
            <p>This section explains how to view your booking history and manage upcoming rides.</p>

            <h3>1. Accessing Your Booking History</h3>
            <p>To view your booking history:</p>
            <ol>
                <li>Log in to your account.</li>
                <li>Navigate to the "My Bookings" section. (This may be a link in the header or in your account dashboard).</li>
            </ol>

            <h3>2. Viewing Booking Details</h3>
            <p>In the "My Bookings" section, you will see a list of your past and upcoming bookings.</p>
            <p>Clicking on a specific booking will show you the details, such as pickup and drop-off locations, date and time, and the total cost.</p>

            <h3>3. Paying Pending Bookings</h3>
            <p>If you have any bookings with a pending payment status, you can pay them directly from the booking list.</p>
            <ol>
                <li>Locate the booking with the "Pending" status.</li>
                <li>Click on the "Pay Now" button next to the booking.</li>
                <li>You'll be directed to the payment page to complete the transaction.</li>
            </ol>
        </section>

        <section id="account" class="help-section">
		    <h2>Account Management</h2>
		    <p>Learn how to manage your profile details and security within Megacity Cab.</p>
		
		    <h3>1. Viewing Your Profile</h3>
		    <p>Your profile provides a summary of your registered information. To view it:</p>
		    <ol>
		        <li>Log in to your Megacity Cab account.</li>
		        <li>Click the "Profile" icon located in the top right corner of the header. This will take you to your profile page.
		            <br><br>
		            <img src="assets/images/customer-profile-nav.png" alt="Screenshot of My Details Page">
		        </li>
		    </ol>
		
		    <h3>2. Updating Your Profile Details</h3>
		    <p>You can modify your profile information such as your name, email, phone number, and address:</p>
		    <ol>
		        <li>Log in to your Megacity Cab account.</li>
		        <li>Click the "Profile" icon located in the top right corner of the header to access your "My Details" page.</li>
		        <li>On your "My Details" page, click the "Update Details" button. This will take you to the profile editing page.</li>
		        <li>Edit the desired fields (name, email address, phone number, address, or NIC).</li>
		        <li>Click the "Save Changes" button to apply your modifications.
		        </li>
		    </ol>
		
		    <h3>3. Changing Your Password</h3>
		    <p>Protect your account by regularly updating your password.  Here's how:</p>
		    <ol>
		        <li>Log in to your Megacity Cab account.</li>
		        <li>Click the "Profile" icon located in the top right corner of the header to access your "My Details" page.</li>
		        <li>On your "My Details" page, click the "Update Details" button. This will take you to the profile editing page.</li>
		        <li>In the profile editing page, you'll find fields to enter and confirm your new password.</li>
		        <li>Enter your new password in the "New Password" field.</li>
		        <li>Re-enter the same new password in the "Confirm New Password" field.</li>
		        <li>Click "Save Changes" to update your password.
		        </li>
		    </ol>
		</section>

        <section id="faq" class="help-section">
            <h2>Frequently Asked Questions</h2>
            <div class="faq-list">
                <div class="faq-item">
                    <h3>How do I cancel a booking?</h3>
                    <p>You can cancel a booking through the "My Bookings" section.  Note that cancellation fees may apply depending on the timing.</p>
                </div>
                <div class="faq-item">
                    <h3>What payment methods are accepted?</h3>
                    <p>We accept major credit cards, debit cards, and digital wallets.</p>
                </div>
                <div class="faq-item">
                    <h3>How do I contact customer support?</h3>
                    <p>You can contact customer support by calling our hotline or sending an email through the "Contact Us" page.</p>
                </div>
            </div>
        </section>
        
        <button id="scrollToTopBtn" title="Go to top"><i class="fas fa-arrow-up"></i></button>

    </main>

    <%@ include file="/WEB-INF/views/components/customerFooter.jsp" %>
    
    <script>
	    //Get the button
	    let scrollToTopBtn = document.getElementById("scrollToTopBtn");
		
		// Get the target section
	    const categorySection = document.querySelector('.help-categories');
	
	    // When the user scrolls down 20px from the top of the document, show the button
	    window.onscroll = function() {scrollFunction()};
	
	    function scrollFunction() {
	      if (document.body.scrollTop > 240 || document.documentElement.scrollTop > 240) {
	        scrollToTopBtn.style.display = "block";
	      } else {
	        scrollToTopBtn.style.display = "none";
	      }
	    }
	
	    // When the user clicks on the button, scroll to the target section
	    scrollToTopBtn.addEventListener('click', () => {
	
			 // Calculate the offset of the target section
	       const targetOffset = categorySection.offsetTop - 80; // Adjust for header
	
	        // Scroll to the section
	         window.scrollTo({
	            top: targetOffset,
	            behavior: 'smooth'
	        });
	    });
	</script>

</body>
</html>