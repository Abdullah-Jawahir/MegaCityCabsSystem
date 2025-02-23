<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Customers - Megacity Cab</title>
    
    <link rel="stylesheet" href="<c:url value='/css/admin.css'/>" >
    
    <script>
	   function showAddCustomerForm() {
		   window.location.href = "<c:url value='/register?tab=customer'/>";
	   }
	</script>
    
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1>Manage Customers</h1>
            <a href="admin" class="back-link">Back to Dashboard</a>
        </header>
        
        <main class="admin-main">
            <div class="action-buttons">
                <button onclick="showAddCustomerForm()">Add New Customer</button>
            </div>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Phone Number</th>
                        <th>Registration Number</th>
                        <th>Address</th>
                        <th>NIC</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="customer" items="${customers}">
                    <tr>
                        <td>${customer.customerId}</td>
                        <td>${customer.user.name}</td> <!-- User data: Full Name -->
                        <td>${customer.user.email}</td> <!-- User data: Email -->
                        <td>${customer.user.phone}</td> <!-- User data: Phone Number -->
                        <td>${customer.registrationNumber}</td> <!-- Customer data: Registration Number -->
                        <td>${customer.address}</td> <!-- Customer data: Address -->
                        <td>${customer.nic}</td> <!-- Customer data: NIC -->
                        <td>
						    <a href="customer?action=editCustomer&customerId=${customer.customerId}">Edit</a>
						    <a href="customer?action=deleteCustomer&customerId=${customer.customerId}" 
						       onclick="return confirm('Are you sure you want to delete this customer?');">Delete</a>
						</td>
                        
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>
