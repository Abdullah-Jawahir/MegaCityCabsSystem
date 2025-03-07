<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.system.model.Booking" %>
<%@ page import="com.system.model.Bill" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment - Megacity Cab</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="<c:url value='/css/payment.css'/>">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <%-- Retrieve attributes from the request --%>
    <%
        Booking booking = (Booking) request.getAttribute("booking");
        Bill bill = (Bill) request.getAttribute("bill");
        Float baseAmount = (Float) request.getAttribute("baseAmount");
        Float taxAmount = (Float) request.getAttribute("taxAmount");
        Float discountAmount = (Float) request.getAttribute("discountAmount");
        Float totalAmount = (Float) request.getAttribute("totalAmount");
    %>

    <div class="payment-container">
        <header class="payment-header">
            <h1>Complete Your Payment</h1>
            <p>Booking ID: <%= booking != null ? booking.getBookingId() : "N/A" %> | Bill ID: <%= bill != null ? bill.getBillId() : "N/A" %></p>
            <div class="header-decoration"></div>
        </header>

        <div class="payment-content">
            <div class="bill-summary">
                <h2><i class="fas fa-receipt"></i> Bill Summary</h2>
                <ul class="bill-details">
                    <li class="bill-item">
                        <span>Base Fare</span>
                        <span>$<%= baseAmount != null ? String.format("%.2f", baseAmount) : "N/A" %></span>
                    </li>
                    <li class="bill-item">
                        <span>Tax</span>
                        <span>$<%= taxAmount != null ? String.format("%.2f", taxAmount) : "N/A" %></span>
                    </li>
                    <li class="bill-item">
                        <span>Discount</span>
                        <span>$<%= discountAmount != null ? String.format("%.2f", discountAmount) : "N/A" %></span>
                    </li>
                    <li class="bill-item total">
                        <span>Total Amount</span>
                        <span>$<%= totalAmount != null ? String.format("%.2f", totalAmount) : "N/A" %></span>
                    </li>
                </ul>
            </div>

            <div class="payment-options">
                <div class="payment-options-header">
                    <h2><i class="fas fa-credit-card"></i> Payment Options</h2>
                    <p>Choose your preferred payment method</p>
                </div>

                <div class="payment-tabs">
                    <div class="payment-tab active" onclick="showTab('card')">
                        <i class="far fa-credit-card"></i> Credit Card
                    </div>
                    <div class="payment-tab" onclick="showTab('cash')">
                        <i class="fas fa-money-bill-wave"></i> Cash
                    </div>
                </div>

                <div id="card-payment" class="payment-tab-content active">
                    <form action="/megacitycabsystem/billing" method="post" class="card-payment-form">
                        <input type="hidden" name="billId" value="<%= bill != null ? bill.getBillId() : "" %>">
                        <input type="hidden" name="bookingId" value="<%= booking != null ? booking.getBookingId() : "" %>">
                        <input type="hidden" name="baseAmount" value="<%= baseAmount %>">
                        <input type="hidden" name="taxAmount" value="<%= taxAmount %>">
                        <input type="hidden" name="totalAmount" value="<%= totalAmount %>">
                        <input type="hidden" name="paymentType" value="card">
                        <input type="hidden" name="action" value="updateRideStatus">

                        <div class="form-group">
                            <label class="form-label" for="cardNumber">Card Number</label>
                            <input type="text" class="form-control" id="cardNumber" name="cardNumber" placeholder="1234 5678 9012 3456" required>
                            <div class="card-icon">
                                <img src="<%= request.getContextPath() %>/assets/images/visa_logo.png" alt="Visa">
								<img src="<%= request.getContextPath() %>/assets/images/mastercard_logo.webp" alt="MasterCard">
								<img src="<%= request.getContextPath() %>/assets/images/american-express_logo.png" alt="American Express">
								<img src="<%= request.getContextPath() %>/assets/images/discover_logo.png" alt="Discover">
                            </div>
                        </div>

                        <div class="card-inputs">
                            <div class="form-group">
                                <label class="form-label" for="expiry">Expiry Date</label>
                                <input type="text" class="form-control" id="expiry" name="expiry" placeholder="MM/YY" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="cvv">CVV</label>
                                <input type="text" class="form-control" id="cvv" name="cvv" placeholder="123" required>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary btn-block">Pay $<%= totalAmount != null ? String.format("%.2f", totalAmount) : "0.00" %> Now</button>
                        <div class="secure-badge">
                            <i class="fas fa-lock"></i> Secured by 256-bit encryption
                        </div>
                    </form>
                </div>

                <div id="cash-payment" class="payment-tab-content">
                    <div class="cash-info">
                        <p><i class="fas fa-info-circle"></i> You will pay the total amount of $<%= totalAmount != null ? String.format("%.2f", totalAmount) : "0.00" %> directly to the driver when your ride is complete.</p>
                    </div>
                    <form action="/megacitycabsystem/billing" method="post">
                        <input type="hidden" name="billId" value="<%= bill != null ? bill.getBillId() : "" %>">
                        <input type="hidden" name="bookingId" value="<%= booking != null ? booking.getBookingId() : "" %>">
                        <input type="hidden" name="baseAmount" value="<%= baseAmount %>">
                        <input type="hidden" name="taxAmount" value="<%= taxAmount %>">
                        <input type="hidden" name="totalAmount" value="<%= totalAmount %>">
                        <input type="hidden" name="paymentType" value="cash">
                        <input type="hidden" name="action" value="updateRideStatus">
                        <button type="submit" class="btn btn-success btn-block">Confirm Cash Payment</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        function showTab(tabName) {
            // Hide all tab contents
            document.querySelectorAll('.payment-tab-content').forEach(tab => {
                tab.classList.remove('active');
            });
            
            // Show the selected tab content
            document.getElementById(tabName + '-payment').classList.add('active');
            
            // Update tab styling
            document.querySelectorAll('.payment-tab').forEach(tab => {
                tab.classList.remove('active');
            });
            
            // Find the clicked tab and make it active
            event.currentTarget.classList.add('active');
        }
    </script>
</body>
</html>