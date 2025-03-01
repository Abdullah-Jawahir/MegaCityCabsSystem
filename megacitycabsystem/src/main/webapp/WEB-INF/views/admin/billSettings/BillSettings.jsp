<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill Configurations - Megacity Cab</title>

    <link rel="stylesheet" href="<c:url value='/css/edit-forms.css'/>">
     <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="form-container">
        <div class="form-card">
            <div class="form-header">
                <h1>Bill Configurations</h1>
                <a href="admin" class="back-link">Back to Dashboard</a>
            </div>
            
             <c:if test="${not empty param.success}">
                <div class="success-message" id="successMessage">
                    <i class="fas fa-check-circle"></i> ${param.success}
                </div>
            </c:if>

            <form action="billSettings" method="post" class="edit-form">
                <div class="form-content">
                    
                    <div class="form-group">
                        <label for="taxRate">Tax Rate</label>
                        <input type="text" 
                               id="taxRate" 
                               name="taxRate"
                               value="${taxRate}"
                               placeholder="Enter the tax rate"
                               pattern="^\d*\.?\d*$"
                               onkeypress="return isNumberKey(event)"
                               onpaste="return validatePaste(event)"
                               required>
                	</div>
                    
                    <div class="form-group">
                        <label for="defaultDiscountRate">Default Discount Rate</label>
                        <input type="text" 
                               id="defaultDiscountRate" 
                               name="defaultDiscountRate"
                               value="${defaultDiscountRate}"
                               placeholder="Enter the default discount rate"
                               pattern="^\d*\.?\d*$"
                               onkeypress="return isNumberKey(event)"
                               onpaste="return validatePaste(event)"
                               required>
                	</div>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn btn-secondary" onclick="history.back()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Update Bill Settings</button>
                </div>
            </form>
        </div>
    </div>
    
   	<script>
        
        function isNumberKey(evt) {
            const charCode = (evt.which) ? evt.which : evt.keyCode;
            if (charCode === 46 && evt.target.value.includes('.')) {
                return false;
            }
            if (charCode !== 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
                return false;
            }
            return true;
        }

        function validatePaste(event) {
            const clipboardData = event.clipboardData || window.clipboardData;
            const pastedData = clipboardData.getData('Text');
            const regex = /^\d*\.?\d*$/;
            return regex.test(pastedData);
        }
        
        window.onload = function() {
            const successMessage = document.getElementById('successMessage');
            if (successMessage) {
                setTimeout(function() {
                    successMessage.classList.add('fade-out');
                     setTimeout(function() {
                        successMessage.style.display = 'none';
                    }, 1000);
                }, 2000); // 2 seconds
            }
        };
    </script>
</body>
</html>