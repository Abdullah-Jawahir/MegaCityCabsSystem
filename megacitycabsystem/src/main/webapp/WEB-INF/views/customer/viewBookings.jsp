<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="com.system.model.Booking" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Bookings - Megacity Cab System</title>
  <link rel="stylesheet" href="css/styles.css">
  <link rel="stylesheet" href="css/viewBookings.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>
  <%@ include file="/WEB-INF/views/components/customerHeader.jsp" %>

  <!-- Main Content -->
  <main class="content-wrapper">
    <h1 class="page-title">My Bookings</h1>

    <!-- Filter Pills -->
    <div class="filter-container">
      <button class="filter-pill active" data-status="all">All Bookings</button>
      <button class="filter-pill" data-status="pending">Pending</button>
      <button class="filter-pill" data-status="assigned">Assigned</button>
      <button class="filter-pill" data-status="in-progress">In Progress</button>
      <button class="filter-pill" data-status="completed">Completed</button>
      <button class="filter-pill" data-status="cancelled">Cancelled</button>
    </div>

    <!-- Booking Cards Container -->
    <div class="bookings-container">
      <c:choose>
          <c:when test="${not empty customerBookings}">
              <c:forEach var="booking" items="${customerBookings}">
            	<%
                   Booking booking = (Booking) pageContext.getAttribute("booking");
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
                   String formattedDateTime = booking.getBookingTime().format(formatter);
                %>
                  <div class="booking-card" data-status="${booking.status}">
                      <div class="booking-header">
                          <span class="booking-id">${booking.bookingId}</span>
                          <span class="booking-time"><%= formattedDateTime %></span>
                          <span class="booking-status ${booking.status}">${booking.status}</span>
                      </div>
                      <div class="booking-content">
                          <div class="booking-route">
                              <div class="location-column">
								  <div class="location-pin">
								    <div class="pin-icon">
								      <i class="fas fa-map-marker-alt pickup-icon"></i>
								    </div>
								    <div class="location-details">
								      <span class="location-type">PICKUP</span>
								      <span class="location-address" data-tooltip="${booking.pickupLocation}">
								        <c:choose>
								          <c:when test="${fn:contains(booking.pickupLocation, ' ')}">
								            <c:forTokens items="${booking.pickupLocation}" delims=" " var="word" varStatus="status">
								              <c:if test="${status.index < 3}">
								                ${word} <c:if test="${not status.last}"> </c:if>
								              </c:if>
								            </c:forTokens>
								            <c:if test="${fn:split(booking.pickupLocation, ' ')[3] != null}">...</c:if>
								          </c:when>
								          <c:otherwise>
								            ${booking.pickupLocation}
								          </c:otherwise>
								        </c:choose>
								      </span>
								    </div>
								  </div>
								</div>
								<div class="location-column">
								  <div class="location-pin">
								    <div class="pin-icon">
								      <i class="fas fa-flag-checkered destination-icon"></i>
								    </div>
								    <div class="location-details">
								      <span class="location-type">DESTINATION</span>
								      <span class="location-address" data-tooltip="${booking.destination}">
								        <c:choose>
								          <c:when test="${fn:contains(booking.destination, ' ')}">
								            <c:forTokens items="${booking.destination}" delims=" " var="word" varStatus="status">
								              <c:if test="${status.index < 3}">
								                ${word} <c:if test="${not status.last}"> </c:if>
								              </c:if>
								            </c:forTokens>
								            <c:if test="${fn:split(booking.destination, ' ')[3] != null}">...</c:if>
								          </c:when>
								          <c:otherwise>
								            ${booking.destination}
								          </c:otherwise>
								        </c:choose>
								      </span>
								    </div>
								  </div>
								</div>
                          </div>

                          <div class="booking-details">
                              <div class="detail-block">
                                  <span class="detail-title">DISTANCE</span>
                                  <span class="detail-value">${booking.distance} km</span>
                              </div>
                              <div class="detail-block">
                                  <span class="detail-title">VEHICLE</span>
                                  <span class="detail-value"><i class="fas fa-car car-icon"></i>${booking.assignedVehicle.model}</span>
                              </div>
                              <div class="detail-block">
                                  <span class="detail-title">PLATE</span>
                                  <span class="detail-value">${booking.assignedVehicle.plateNumber}</span>
                              </div>
                          </div>

                          <div class="booking-driver">
                              <div class="driver-avatar">
                                  <i class="fas fa-user driver-icon"></i>
                              </div>
                              <div class="driver-info">
                                  <div class="driver-name">${booking.assignedDriver.user.name}</div>
                                  <div class="driver-car">${booking.assignedDriver.user.phone}</div>
                              </div>
                              <c:choose>
								    <c:when test="${booking.status == 'completed'}">
								        <form action="billing" method="post" target="_blank">
								            <input type="hidden" name="action" value="printBill">
								            <input type="hidden" name="bookingId" value="${booking.bookingId}">
								            <button class="btn btn-print" type="submit">
								                <i class="fas fa-print"></i> Print
								            </button>
								        </form>
								    </c:when>
								    <c:otherwise>
								        <button class="btn btn-print disabled" disabled>
								            <i class="fas fa-print"></i> Print
								        </button>
								    </c:otherwise>
								</c:choose>
                          </div>

                       	<div class="booking-actions">
			              <c:if test="${booking.status == 'pending'}">
			                <form action="billing" method="post">
			                  <input type="hidden" name="action" value="viewPendingBill">
			                  <input type="hidden" name="bookingId" value="${booking.bookingId}">
			                  <button type="submit" class="btn btn-primary">Pay Now</button>
			                </form>
			              </c:if>
			              <c:choose>
			              	<c:when test="${canCancelMap[booking.bookingId] == true}">
				              	<form action="booking" method="post">
				                  <input type="hidden" name="action" value="cancelBooking">
				                  <input type="hidden" name="bookingId" value="${booking.bookingId}">
				                  <button type="submit" class="btn btn-danger">Cancel Booking</button>
				              	</form>
			              	</c:when>
			              	<c:otherwise>
			              		<button class="btn btn-danger disabled" disabled>Cancel Booking</button>
			              	</c:otherwise>
			              </c:choose>
		            	</div>
                      </div>
                  </div>
              </c:forEach>
          </c:when>
      </c:choose>
    </div>
  </main>

  <%@ include file="/WEB-INF/views/components/customerFooter.jsp" %>

  <script>
	  document.addEventListener('DOMContentLoaded', function() {
	    const filterPills = document.querySelectorAll('.filter-pill');
	    const bookingCards = document.querySelectorAll('.booking-card');
	    const bookingsContainer = document.querySelector('.bookings-container');
	    let noBookingsDiv = document.querySelector('.no-bookings'); // Select No bookings element initially.

	    filterPills.forEach(pill => {
	      pill.addEventListener('click', function() {
	        // Remove active class from all pills
	        filterPills.forEach(p => p.classList.remove('active'));

	        // Add active class to the clicked pill
	        this.classList.add('active');

	        const selectedStatus = this.dataset.status;

	        // Show/hide booking cards based on selected status
	        bookingCards.forEach(card => {
	          if (selectedStatus === 'all' || card.dataset.status === selectedStatus) {
	            card.style.display = 'block';
	          } else {
	            card.style.display = 'none';
	          }
	        });

	        // Check if there are any visible bookings after filtering.
	        const visibleBookings = Array.from(bookingCards).filter(card => card.style.display !== 'none');

            // Handle no bookings message. You'll need to show/hide based on the cards.
            if (visibleBookings.length === 0) {
              // Create No-bookings section only if it doesn't already exist
              if (!noBookingsDiv) {
                noBookingsDiv = document.createElement('div');
                noBookingsDiv.classList.add('no-bookings');
                noBookingsDiv.innerHTML = `
                  <i class="far fa-calendar-times"></i>
                  <h3>No Bookings Available</h3>
                  <p>No bookings found with the selected filter.</p>
                  <a href="home" class="btn btn-primary">Book a Cab</a>
                `;
                bookingsContainer.appendChild(noBookingsDiv);
              } else {
                noBookingsDiv.style.display = 'block'; // Ensure it's visible if it exists
              }
              bookingCards.forEach(card => card.style.display = 'none'); // hide all cards
            } else {
              // If bookings are visible, remove the "No Bookings" message if it exists
              if (noBookingsDiv) {
                noBookingsDiv.style.display = 'none';
              }
              bookingCards.forEach(card => {
                if (selectedStatus === 'all' || card.dataset.status === selectedStatus) {
                  card.style.display = 'block'; // Make sure these cards are visible
                }
              });

            }
          });
        });

        // Initially hide the "No Bookings Available" message if there are bookings present
        if (bookingCards.length > 0 && noBookingsDiv) {
          noBookingsDiv.style.display = 'none';
        }
      });
    </script>
</body>
</html>