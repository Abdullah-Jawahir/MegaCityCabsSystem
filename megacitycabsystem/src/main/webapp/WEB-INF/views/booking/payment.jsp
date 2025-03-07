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
                                <img src="<%= request.getContextPath() %>/assets/images/visa_logo.png" alt="visa">
								<img src="<%= request.getContextPath() %>/assets/images/mastercard_logo.webp" alt="mastercard">
								<img src="<%= request.getContextPath() %>/assets/images/american-express_logo.png" alt="amex">
								<img src="<%= request.getContextPath() %>/assets/images/discover_logo.png" alt="discover">
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
        
     // Credit Card Validation Script
        document.addEventListener('DOMContentLoaded', function() {
            const cardNumberInput = document.getElementById('cardNumber');
            const expiryInput = document.getElementById('expiry');
            const cvvInput = document.getElementById('cvv');
            const cardForm = document.querySelector('.card-payment-form');
            
            // Add input event listeners for real-time validation
            cardNumberInput.addEventListener('input', function(e) {
                formatCardNumber(e.target);
                validateCardNumber(e.target);
            });
            
            expiryInput.addEventListener('input', function(e) {
                formatExpiry(e.target);
                validateExpiry(e.target);
            });
            
            cvvInput.addEventListener('input', function(e) {
                validateCVV(e.target);
            });
            
            // Add form submission validation
            cardForm.addEventListener('submit', function(e) {
                if (!validateForm()) {
                    e.preventDefault();
                }
            });
            
         	// Format card number with spaces and limit length
            function formatCardNumber(input) {
                let value = input.value.replace(/\D/g, '');
                
                // Detect card type to determine max length
                let cardType = getCardType(value.substring(0, 6));
                let maxLength = 16;
                
                // American Express has 15 digits
                if (value.startsWith('34') || value.startsWith('37')) {
                    maxLength = 15;
                }
                // Some cards like Diners Club have 14 digits
                else if (value.startsWith('36') || value.startsWith('38') || value.startsWith('39')) {
                    maxLength = 14;
                }
                
                // Truncate to max length
                value = value.substring(0, maxLength);
                
                let formattedValue = '';
                
                for (let i = 0; i < value.length; i++) {
                    if (i > 0 && i % 4 === 0) {
                        formattedValue += ' ';
                    }
                    formattedValue += value[i];
                }
                
                input.value = formattedValue.trim();
            }
            
            // Format expiry date as MM/YY
            function formatExpiry(input) {
                let value = input.value.replace(/\D/g, '');
                let formattedValue = '';
                
                if (value.length > 0) {
                    formattedValue = value.substring(0, 2);
                    if (value.length > 2) {
                        formattedValue += '/' + value.substring(2, 4);
                    }
                }
                
                input.value = formattedValue;
            }
            
         	// Validate card number using Luhn algorithm and identify card type
            function validateCardNumber(input) {
                const value = input.value.replace(/\s/g, '');
                const errorElement = getOrCreateErrorElement(input);

                // Clear previous error and validation classes
                errorElement.textContent = '';
                input.classList.remove('invalid');
                input.classList.remove('valid'); 

                // Check if empty
                if (!value) {
                    return false;
                }

                // Check length first
                if (value.length < 13 || value.length > 19) {
                    errorElement.textContent = 'Card number must be 13-19 digits';
                    input.classList.add('invalid');
                    return false;
                }

                // Identify card type and validate format
                let cardType = 'mastercard';
                if (!cardType) {
                    errorElement.textContent = 'Invalid card number format';
                    input.classList.add('invalid');
                    return false;
                }

                // Validate using Luhn algorithm
                /* if (!luhnCheck(value)) {
                    errorElement.textContent = 'Invalid card number';
                    input.classList.add('invalid');
                    return false;
                } */

                // Highlight the corresponding card icon
                highlightCardType(cardType);

                // Mark as valid
                input.classList.add('valid');
                return true;
            }
            
            // Validate expiry date
            function validateExpiry(input) {
                const value = input.value;
                const errorElement = getOrCreateErrorElement(input);
                
                // Clear previous error
                errorElement.textContent = '';
                input.classList.remove('invalid');
                input.classList.remove('valid'); 
                
                // Check if empty
                if (!value) {
                    return false;
                }
                
                // Check format
                if (!/^\d{2}\/\d{2}$/.test(value)) {
                    errorElement.textContent = 'Format: MM/YY';
                    input.classList.add('invalid');
                    return false;
                }
                
                // Extract month and year
                const [month, year] = value.split('/').map(num => parseInt(num, 10));
                
                // Validate month
                if (month < 1 || month > 12) {
                    errorElement.textContent = 'Invalid month';
                    input.classList.add('invalid');
                    return false;
                }
                
                // Get current date
                const currentDate = new Date();
                const currentYear = currentDate.getFullYear() % 100;
                const currentMonth = currentDate.getMonth() + 1;
                
                // Check if card is expired
                if (year < currentYear || (year === currentYear && month < currentMonth)) {
                    errorElement.textContent = 'Card expired';
                    input.classList.add('invalid');
                    return false;
                }
                
             	// Mark as valid
                input.classList.add('valid');
                return true;
            }
            
         	// Validate CVV
            function validateCVV(input) {
                // Remove non-digit characters
                let value = input.value.replace(/\D/g, '');
                const errorElement = getOrCreateErrorElement(input);
                
                // Clear previous error
                errorElement.textContent = '';
                input.classList.remove('invalid');
                input.classList.remove('valid'); 
                
                // Get card number to determine expected CVV length
                const cardNumber = document.getElementById('cardNumber').value.replace(/\s/g, '');
                
                // Determine if it's American Express (starts with 34 or 37)
                const isAmex = /^3[47]/.test(cardNumber);
                const expectedLength = isAmex ? 4 : 3;
                
                // Truncate to expected length if user types more
                if (value.length > expectedLength) {
                    value = value.substring(0, expectedLength);
                    input.value = value; // Update the input field
                }
                
                // Check if empty
                if (!value) {
                    errorElement.textContent = 'CVV is required';
                    input.classList.add('invalid');
                    return false;
                }
                
                // Validate CVV length
                if (value.length !== expectedLength) {
                    errorElement.textContent = `${expectedLength} digits required`;
                    input.classList.add('invalid');
                    return false;
                }
                
                // Mark as valid
                input.classList.add('valid');
                return true;
            }
            
            // Get card type based on starting digits and length
            function getCardType(cardNumber) {
                const regexPatterns = {
                    visa: /^4[0-9]{12}(?:[0-9]{3})?$/,
                    mastercard: /^5[1-5][0-9]{14}$/,
                    amex: /^3[47][0-9]{13}$/,
                    discover: /^6(?:011|5[0-9]{2})[0-9]{12}$/
                };
                
                for (const [type, regex] of Object.entries(regexPatterns)) {
                    if (regex.test(cardNumber)) {
                        return type;
                    }
                }
                
                return null;
            }
            
            // Highlight the card type icon
            function highlightCardType(cardType) {
                // Reset all card icons
                const cardIcons = document.querySelectorAll('.card-icon img');
                cardIcons.forEach(icon => {
                    icon.style.opacity = '0.3';
                });
                
                // Highlight the detected card type
                const cardIcon = document.querySelector(`.card-icon img[alt="${cardType}"]`);
                if (cardIcon) {
                    cardIcon.style.opacity = '1';
                }
            }
            
            // Create or get error element
            function getOrCreateErrorElement(input) {
                let errorElement = input.parentElement.querySelector('.error-message');
                
                if (!errorElement) {
                    errorElement = document.createElement('div');
                    errorElement.className = 'error-message';
                    errorElement.style.color = '#e74c3c';
                    errorElement.style.fontSize = '12px';
                    errorElement.style.marginTop = '5px';
                    input.parentElement.appendChild(errorElement);
                }
                
                return errorElement;
            }
            
            // Luhn algorithm for credit card validation
            function luhnCheck(cardNumber) {
                if (!cardNumber) return false;
                
                let sum = 0;
                let shouldDouble = false;
                
                // Loop through values starting from the rightmost digit
                for (let i = cardNumber.length - 1; i >= 0; i--) {
                    let digit = parseInt(cardNumber.charAt(i));
                    
                    if (shouldDouble) {
                        digit *= 2;
                        if (digit > 9) digit -= 9;
                    }
                    
                    sum += digit;
                    shouldDouble = !shouldDouble;
                }
                
                return (sum % 10) === 0;
            }
            
            // Validate the entire form before submission
            function validateForm() {
                const isCardValid = validateCardNumber(cardNumberInput);
                const isExpiryValid = validateExpiry(expiryInput);
                const isCvvValid = validateCVV(cvvInput);
                
                return isCardValid && isExpiryValid && isCvvValid;
            }
        });
    </script>
</body>
</html>