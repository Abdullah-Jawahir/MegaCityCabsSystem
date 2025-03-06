<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Admin Support - Megacity Cab</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="<c:url value='/css/dashboard.css'/>"> <!-- General Styles -->
    <link rel="stylesheet" href="<c:url value='/css/adminSupport.css'/>"> <!-- Specific Admin Support Styles -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    
</head>
<body>
    <div class="admin-container">
         <%@ include file="/WEB-INF/views/components/sidebar.jsp" %>

        <main class="main-content">
            <%@ include file="/WEB-INF/views/components/header.jsp" %>

            <div class="dashboard-content">

                <main class="support-page">
                    <section class="hero">
                        <h1>How can we help you manage Megacity Cab?</h1>
                        <p>Find answers to common questions and guides on using the Admin Dashboard effectively.</p>
                    </section>

                    <section class="help-categories">
                        <h2>Browse by Category</h2>
                        <div class="category-grid">

                            <div class="category-card">
                                <i class="fas fa-tachometer-alt"></i>
                                <h3>Understanding the Dashboard</h3>
                                <p>Learn how to interpret the key metrics and configure the dashboard settings.</p>
                                <a href="#dashboard" class="read-more">Read More</a>
                            </div>
                            
                            <div class="category-card">
                                <i class="fa-solid fa-user-tie"></i>
                                <h3>Update Admin Profile</h3>
                                <p>Guides on updating your administrator account.</p>
                                <a href="#admin-profile-update" class="read-more">Read More</a>
                            </div>

                            <div class="category-card">
                                <i class="fas fa-users"></i>
                                <h3>Managing Customers</h3>
                                <p>Guides on adding, editing, and deleting customer and administrator accounts.</p>
                                <a href="#user-management" class="read-more">Read More</a>
                            </div>

                            <div class="category-card">
                                <i class="fas fa-calendar-check"></i>
                                <h3>Managing Bookings</h3>
                                <p>How to view, edit, cancel, and resolve issues related to bookings.</p>
                                <a href="#booking-management" class="read-more">Read More</a>
                            </div>

                             <div class="category-card">
                                <i class="fas fa-user-tie"></i>
                                <h3>Managing Drivers</h3>
                                <p>Guides on adding, editing, verifying, and managing driver accounts.</p>
                                <a href="#driver-management" class="read-more">Read More</a>
                            </div>

                            <div class="category-card">
                                <i class="fas fa-taxi"></i>
                                <h3>Managing Vehicles</h3>
                                <p>How to add, edit, and track vehicles in the system.</p>
                                <a href="#vehicle-management" class="read-more">Read More</a>
                            </div>

                            <div class="category-card">
                                <i class="fas fa-cog"></i>
                                <h3>Bill Configuration</h3>
                                <p>Guides for configuring the bill setting or rate settings</p>
                                <a href="#system-configuration" class="read-more">Read More</a>
                            </div>

                        </div>
                    </section>

                    <section id="dashboard" class="help-section">
                        <h2>Understanding the Admin Dashboard</h2>
                        <p>The Admin Dashboard provides a centralized view of key performance indicators (KPIs) for the Megacity Cab system.  It allows you to monitor the overall health of the business and make informed decisions.</p>

                        <h3>Key Metrics</h3>
                        <ul>
                            <li><strong>Total Customers:</strong> The total number of registered customers in the system.</li>
                            <li><strong>Active Bookings:</strong> The number of bookings currently in progress.</li>
                            <li><strong>Available Vehicles:</strong> The number of vehicles currently available for bookings.</li>
                            <li><strong>Period Revenue:</strong> The total revenue generated within the selected time period.</li>
                        </ul>

                        <h3>Customizing the Dashboard</h3>
                        <p>You can customize the dashboard's time period by using the dropdown menu in the top right corner.  Available options include Last 7 Days, Last 30 Days, This Month, and This Year.</p>
                    </section>
                    
                    <section id="admin-profile-update" class="help-section">
					    <h2>Updating Your Admin Profile</h2>
					    <p>This section explains how to access and update your administrator profile within the Megacity Cab system. Keeping your profile information up-to-date is important for security and communication purposes.</p>
					
					    <h3>Accessing Your Admin Profile</h3>
					    <ol>
					        <li>Locate your profile icon in the header of the Admin Dashboard.  This is typically located in the upper-right corner.</li>
					        <li>Click on your profile icon.  This will navigate you to your profile page.</li>
					    </ol>
					    <img src="assets/images/admin-profile-nav.png" alt="Admin Profile Icon preview"/>
					
					    <h3>Viewing Your Profile Details</h3>
					    <p>Once you access your profile, you will see a page displaying your current profile information.  This may include your name, email address, contact number, and other relevant details.</p>
					
					    <h3>Navigating to the Profile Update Page</h3>
					    <p>To update your profile information, look for an "Update Profile" button on your profile page.  Clicking this button will take you to the profile update page.</p>
					
					    <h3>Updating Your Profile Information</h3>
					    <ol>
					        <li>On the profile update page, you will be able to modify the editable fields in your profile.</li>
					        <li>Make the necessary changes to your name, email address, contact number, or other relevant information.</li>
					        <li>Ensure that all required fields are filled in correctly.</li>
					        <li>Review the updated information carefully.</li>
					    </ol>
					
					    <h3>Saving Your Changes</h3>
					    <p>After updating your profile information, click the "Save Changes" button to save the changes.  A confirmation message may be displayed to indicate that your profile has been updated successfully.</p>
					</section>

                    <section id="user-management" class="help-section">
                        <h2>Managing Users</h2>
                        <p>This section provides guidance on managing both customer and administrator accounts.</p>

                        <h3>Registering a New Customer</h3>
                        <ol>
                            <li>Navigate to the "Customers" section in the sidebar.</li>
                            <li>Click the "Add New Customer" button.</li>
                            <li>Fill in the required fields, including name, email address, and password.</li>
                            <li>Click the "Register" button.</li>
                        </ol>

                        <h3>Editing a Customer Account</h3>
                        <ol>
                            <li>Navigate to the "Customers" section.</li>
                            <li>Find the customer you want to edit in the list.</li>
                            <li>Click the "Edit" button on that record.</li>
                            <li>Make the necessary changes to their profile.</li>
                            <li>Click the "Update Customer" button.</li>
                        </ol>

                        <h3>Deleting a Customer Account</h3>
                        <p>Deleting a customer account will prevent them from logging in and using the system..</p>
                        <ol>
                            <li>Navigate to the "Customers" section.</li>
                            <li>Find the customer you want to delete.</li>
                            <li>Click the "Delete" button.</li>
                            <li>Confirm the deletion.</li>
                        </ol>
                    </section>

                   	<section id="booking-management" class="help-section">
					    <h2>Managing Bookings</h2>
					    <p>This section covers the procedures for managing existing bookings within the system, as well as creating new bookings on behalf of customers.</p>
					
					    <h3>Viewing Booking Details</h3>
					    <ol>
					        <li>Navigate to the "Bookings" section in the Admin Dashboard.</li>
					        <li>Use the search filters (e.g., booking ID, customer name, date range) to find the specific booking you want to view.</li>
					    </ol>
					
					    <h3>Creating a Booking on Behalf of a Customer</h3>
					    <p>Administrators can create bookings for customers who may be having trouble using the app or website, or who prefer to book over the phone.  When creating a booking on behalf of a customer, the payment will initially be marked as "pending," and the customer will need to pay later.</p>
					    <ol>
					        <li>Navigate to the "Bookings" section.</li>
					        <li>Click the "Add Booking" button.</li>
					        <li>Select the customer for whom you are creating the booking. You may need to search for the customer by name.</li>
					        <li>Enter the pickup and drop-off locations.</li>
					        <li>Select the date and time of the booking.</li>
					        <li>Choose a vehicle type.</li>
					        <li>Review all the details carefully.</li>
					        <li>Click "Create Booking".</li>
					        <li>Inform the customer that their booking has been created and that they will need to complete the payment through the app or website. Provide the customer with the booking ID.</li>
					    </ol>
					    <p><strong>Important Note:</strong> Ensure you inform the customer of the booking details and the pending payment status. It is advisable to have a clear process for communicating pending payments and sending payment reminders.</p>
					
					    <h3>Canceling a Booking (as Admin)</h3>
					    <p>As an administrator, you can cancel bookings if necessary. Be sure to follow the proper procedures for notifying the customer and processing any refunds according to company policy.</p>
					    <ol>
					        <li>View the booking details as described above.</li>
					        <li>Click the "Cancel Booking" button.</li>
					        <li>Select a reason for the cancellation from the available options.</li>
					        <li>Confirm the cancellation.</li>
					        <li>The system will automatically process the refund (if applicable) based on the cancellation policies.</li>
					        <li>Notify the customer of the cancellation and any refund details.</li>
					    </ol>
					
					    <h3>Editing a Booking</h3>
					    <p>Administrators can edit existing bookings to correct errors, update information, or accommodate customer requests. Common edits include changing the pickup time, drop-off location, or vehicle type.</p>
					    <ol>
					        <li>Navigate to the "Bookings" section and find the booking you wish to edit.</li>
					        <li>Click the "Edit Booking" button (or similar) associated with that booking.</li>
					        <li>Make the necessary changes to the booking details.</li>
					        <li>Click "Save Changes".</li>
					        <li>Notify the customer of any significant changes made to their booking.</li>
					    </ol>
					        <p><strong>Warning:</strong> Be cautious when editing bookings, and make sure the customer accepts all changes. Failure to obtain customer's consent would cause complaints and loss of customer trust.</p>
					
					    <h3>Deleting a Booking</h3>
					    <p>Deleting a booking should be reserved for exceptional circumstances, such as duplicate bookings or fraudulent activity. It is generally preferable to cancel a booking rather than delete it, as canceling preserves a record of the booking for auditing purposes.</p>
					    <ol>
					        <li>Navigate to the "Bookings" section and find the booking you wish to delete.</li>
					         <li>Locate and click the "Delete" button for that booking. (Make sure to check if this function is configured)</li>
					        <li>You will be prompted to confirm the deletion. Confirm the deletion only if you are certain it is the correct action.</li>
					    </ol>
					    <p><strong>Warning:</strong> Deleting a booking is irreversible and removes all record of the booking from the system. Exercise extreme caution when deleting bookings, and only do so when absolutely necessary.</p>
					
					</section>

                    <section id="driver-management" class="help-section">
                        <h2>Managing Drivers</h2>
                        <p>This section details how to manage drivers within the system.</p>

                        <h3>Adding a New Driver</h3>
                        <ol>
                            <li>Navigate to the "Drivers" section.</li>
                            <li>Click the "Add New Driver" button.</li>
                            <li>Fill in the required fields: name, contact information, license details, vehicle assignment, insurance information, and payment details.</li>
                            <li>Upload all requested documents</li>
                            <li>Click "Submit for Verification".</li>
                        </ol>

                        <h3>Editing a Driver's Profile</h3>
                        <ol>
                            <li>Navigate to the "Drivers" section.</li>
                            <li>Locate the driver's profile.</li>
                            <li>Click "Edit Driver" to open the profile editing form.</li>
                            <li>Make the necessary changes.</li>
                            <li>Click "Save Changes".</li>
                        </ol>

                        <h3>Deactivating/Terminating a Driver Account</h3>
                        <p>Driver deactivation/termination is irreversible. Exercise caution when terminating an account, and ensure proper steps are followed, including informing the driver.</p>
                        <ol>
                            <li>Navigate to the "Drivers" section.</li>
                            <li>Locate the driver's profile.</li>
                            <li>Click "Deactivate" button.</li>
                            <li>Confirm the action.</li>
                        </ol>

                    </section>

                    <section id="vehicle-management" class="help-section">
                        <h2>Managing Vehicles</h2>
                         <p>This section details how to manage the vehicles within the system.</p>

                        <h3>Adding a New Vehicle</h3>
                        <ol>
                            <li>Navigate to the "Vehicles" section.</li>
                            <li>Click the "Add New Vehicle" button.</li>
                            <li>Fill in the required fields: vehicle type, make, model, year, license plate</li>
                            <li>Click "Add Vehicle".</li>
                        </ol>

                        <h3>Editing Vehicle Details</h3>
                         <ol>
                            <li>Navigate to the "Vehicles" section.</li>
                            <li>Locate the vehicle's profile.</li>
                            <li>Click "Edit Vehicle" to open the profile editing form.</li>
                            <li>Make the necessary changes.</li>
                            <li>Click "Update Vehicle".</li>
                        </ol>

                        <h3>Removing a Vehicle from the System</h3>
                        <ol>
                            <li>Navigate to the "Vehicles" section.</li>
                            <li>Locate the vehicle's profile.</li>
                            <li>Click "Deactivate" button.</li>
                            <li>Confirm the action.</li>
                        </ol>

                    </section>
                    
                    <section id="bill-configuration" class="help-section">
					    <h2>Bill Configuration</h2>
					    <p>This section explains how to configure the key settings that affect how bills are calculated and presented to customers.  These settings directly impact revenue and customer satisfaction, so it's important to understand them thoroughly.</p>
					
					    <h3>Accessing Bill Configuration Settings</h3>
					    <ol>
					        <li>Log in to the Admin Dashboard.</li>
					        <li>Navigate to the "Bill Configuration" section</li>
					    </ol>
					
					    <h3>Configuring the Tax Rate</h3>
					    <p>The tax rate is a percentage that is added to the base fare and any applicable surcharges. It is a crucial setting for ensuring compliance with local tax regulations.</p>
					    <ol>
					        <li>In the "Bill Configuration" settings, locate the "Tax Rate" field.</li>
					        <li>Enter the tax rate as a decimal (e.g., 0.08 for 8%).</li>
					        <li>Click "Save" or "Update" to apply the changes.</li>
					    </ol>
					    <p><strong>Important Note:</strong> Ensure that the tax rate is compliant with all applicable local and national regulations. Incorrect tax settings can lead to legal issues.</p>
					
					    <h3>Setting the Default Discount Rate</h3>
					    <p>The default discount rate is a percentage that is automatically applied to all bills. This setting can be used to offer general promotions or loyalty rewards to customers.</p>
					    <ol>
					        <li>In the "Bill Configuration" settings, locate the "Default Discount Rate" field.</li>
					        <li>Enter the discount rate as a decimal (e.g., 0.05 for 5%).  Enter 0 if you don't want to apply any discount by default.</li>
					        <li>Click "Save" or "Update" to apply the changes.</li>
					    </ol>
					    <p><strong>Important Note:</strong> Be mindful of the impact that discounts have on your overall revenue.  Track the usage of discounts and their effect on customer behavior to optimize your pricing strategy.</p>
					
					    <h3>Best Practices</h3>
					    <ul>
					        <li><strong>Regularly Review Settings:</strong> Review your bill configuration settings periodically (e.g., quarterly) to ensure they are still accurate and appropriate for your business.</li>
					        <li><strong>Track the Impact of Changes:</strong>  Monitor your revenue and customer satisfaction after making changes to the bill configuration. This will help you understand the effects of your changes and optimize your settings.</li>
					        <li><strong>Communicate Changes to Customers:</strong>  If you make significant changes to your billing practices, notify your customers in advance to avoid confusion or complaints.</li>
					    </ul>
					</section>

                    <section id="faq" class="help-section">
                        <h2>Frequently Asked Questions</h2>
                        <div class="faq-list">
                            <div class="faq-item">
                                <h3>How do I reset a driver's password?</h3>
                                <p>Go to the "Drivers" section, find the driver, click "Edit," and use the password reset option.</p>
                            </div>
                            <div class="faq-item">
                                <h3>How do I generate a financial report for a specific period?</h3>
                                <p>Go to the "Reports" section and specify the date range.</p>
                            </div>
                             <div class="faq-item">
                                <h3>How do I upload a new vehicle type?</h3>
                                <p>Go to the "Vehicle" section, select the add new vehicle button to begin.</p>
                            </div>
                        </div>
                    </section>

                </main>

            </div>

        </main>
        
        <button id="scrollToTopBtn" title="Go to top"><i class="fas fa-arrow-up"></i></button>
    </div>
    
    <script>
	    //Get the button
	    let scrollToTopBtn = document.getElementById("scrollToTopBtn");
		
		// Get the target section
	    const categorySection = document.querySelector('.help-categories');
	
	    // When the user scrolls down 20px from the top of the document, show the button
	    window.onscroll = function() {scrollFunction()};
	
	    function scrollFunction() {
	      if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
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