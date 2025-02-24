<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Customers - Megacity Cab</title>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!-- Make sure dashboard.css is loaded first -->
    <link rel="stylesheet" href="<c:url value='/css/dashboard.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/managePanel.css'/>">
    <!-- Add Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="admin-container">
        <%@ include file="/WEB-INF/views/components/sidebar.jsp" %>
        <main class="main-content">
    		<%@ include file="/WEB-INF/views/components/header.jsp" %>
    		<div class="dashboard-content">
	            <div class="action-buttons">
                    <button onclick="showAddCustomerForm()">
                        <i class="fas fa-plus"></i> Add New Customer
                    </button>
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
	                        <td>${customer.user.name}</td>
	                        <td>${customer.user.email}</td>
	                        <td>${customer.user.phone}</td>
	                        <td>${customer.registrationNumber}</td>
	                        <td>${customer.address}</td>
	                        <td>${customer.nic}</td>
	                        <td>
							    <a href="customer?action=editCustomer&customerId=${customer.customerId}">Edit</a>
							    <a href="customer?action=deleteCustomer&customerId=${customer.customerId}" class="delete-action" 
							       onclick="return confirm('Are you sure you want to delete this customer?');">Delete</a>
							</td>
	                        
	                    </tr>
	                </c:forEach>
	                </tbody>
	            </table>
	        </div>
        </main>
    </div>
    
    <script>
	   function showAddCustomerForm() {
		   window.location.href = "<c:url value='/register?tab=customer'/>";
	   }
	</script>
</body>
</html>