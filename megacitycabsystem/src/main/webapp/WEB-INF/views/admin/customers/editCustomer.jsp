<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Customer - Megacity Cab</title>
    
    <link rel="stylesheet" href="<c:url value='/css/edit-forms.css'/>">
</head>
<body>
    <div class="form-container">
        <div class="form-card">
            <div class="form-header">
                <h1>Edit Customer</h1>
                <a href="customer?action=manageCustomers" class="back-link">Back to Customers</a>
            </div>

            <form action="customer?action=updateCustomer" method="post">
                <input type="hidden" name="action" value="updateCustomer">
                <input type="hidden" name="customerId" value="${customer.customerId}">

                <div class="form-content">
                    <div class="form-group">
                        <label for="name">Full Name</label>
                        <input type="text" name="name" id="name" value="${customer.user.name}" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" name="email" id="email" value="${customer.user.email}" required>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone Number</label>
                        <input type="tel" name="phone" id="phone" value="${customer.user.phone}" required>
                    </div>

                    <div class="form-group">
                        <label for="registrationNumber">Registration Number</label>
                        <input type="text" name="registrationNumber" id="registrationNumber" value="${customer.registrationNumber}" required>
                    </div>

                    <div class="form-group">
                        <label for="address">Address</label>
                        <input type="text" name="address" id="address" value="${customer.address}" required>
                    </div>

                    <div class="form-group">
                        <label for="nic">NIC</label>
                        <input type="text" name="nic" id="nic" value="${customer.nic}" required>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn btn-secondary" onclick="history.back()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Update Customer</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>