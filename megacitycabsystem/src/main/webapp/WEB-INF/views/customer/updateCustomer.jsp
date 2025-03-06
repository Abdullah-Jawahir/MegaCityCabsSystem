<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Customer Profile</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/updateCustomer.css'/>">  <%--Separate CSS file--%>

</head>
<body>
    <div class="container">
        <form action="customer?action=updateCustomer" method="post" class="update-profile-card">
            <div class="card-header">
                <div class="profile-avatar-container">
                    <div class="profile-avatar">
                        <i class="fas fa-user-shield"></i>
                    </div>
                    <div class="edit-avatar">
                        <i class="fas fa-pencil-alt"></i>
                    </div>
                </div>
                <h1>Update Your Profile</h1>
                <p>Change your profile information below</p>
            </div>

            <c:if test="${not empty error}">
                <div id="error-message" class="error-popup">${error}</div>
            </c:if>
            <input type="hidden" name="customerId" value="${customer.customerId}" />
            <div class="card-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label for="name" class="form-label">Full Name</label>
                        <div class="input-group">
                            <i class="fas fa-user input-icon"></i>
                            <input type="text" id="name" name="name" class="form-control input-with-icon" value="${user.name}" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="username" class="form-label">Username</label>
                        <div class="input-group">
                            <i class="fas fa-id-badge input-icon"></i>
                            <input type="text" id="username" name="username" class="form-control input-with-icon" value="${user.username}" required>
                        </div>
                    </div>

                    <div class="form-group full-width">
                        <label for="email" class="form-label">Email Address</label>
                        <div class="input-group">
                            <i class="fas fa-envelope input-icon"></i>
                            <input type="email" id="email" name="email" class="form-control input-with-icon" value="${user.email}" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="phone" class="form-label">Phone Number</label>
                        <div class="input-group">
                            <i class="fas fa-phone input-icon"></i>
                            <input type="tel" id="phone" name="phone" class="form-control input-with-icon" value="${user.phone}" required>
                        </div>
                    </div>

                    <div class="form-group full-width">
                        <label for="address" class="form-label">Address</label>
                        <div class="input-group">
                            <i class="fas fa-map-marker-alt input-icon"></i>
                            <input type="text" id="address" name="address" class="form-control input-with-icon" value="${customer.address}" required>
                        </div>
                    </div>

                    <div class="form-group full-width">
                        <label for="nic" class="form-label">NIC</label>
                        <div class="input-group">
                            <i class="fas fa-address-card input-icon"></i>
                            <input type="text" id="nic" name="nic" class="form-control input-with-icon" value="${customer.nic}" required>
                        </div>
                    </div>
                      
                     <div class="form-group full-width">
                        <label for="newPassword" class="form-label">New Password (leave blank to keep current)</label>
                        <div class="input-group">
                            <i class="fas fa-lock input-icon"></i>
                            <input type="password" id="newPassword" name="newPassword" class="form-control input-with-icon" placeholder="Enter new password">
                        </div>
                    </div>
                    
                    <div class="form-group full-width">
                        <label for="confirmPassword" class="form-label">Confirm New Password</label>
                        <div class="input-group">
                            <i class="fas fa-lock input-icon"></i>
                            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control input-with-icon" placeholder="Confirm new password">
                        </div>
                    </div>

                </div>
            </div>

            <div class="card-footer">
                <a href="customer?action=viewProfile" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var errorMessage = document.getElementById('error-message');
        if (errorMessage) {
            // Trigger the animation
            errorMessage.classList.add('show');

            // Hide the message after 5 seconds (5000 milliseconds)
            setTimeout(function() {
                errorMessage.classList.remove('show');
                // Optionally, remove the element from the DOM after the animation
                setTimeout(function() {
                    errorMessage.remove();
                }, 500); // Wait for the fade-out animation
            }, 5000);
        }
    });
</script>
</body>
</html>