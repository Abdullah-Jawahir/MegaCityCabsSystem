<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="top-header">
    <div class="header-search">
        <i class="fas fa-search"></i>
        <input type="text" placeholder="Search...">
    </div>
    <div class="header-right">
        <div class="notifications">
            <i class="fas fa-bell"></i>
            <span class="badge">3</span>
        </div>
        <div class="admin-profile">
            <img src="<c:url value='/assets/images/user-profile.avif'/>" alt="Admin">
            <span>Admin Name</span>
        </div>
    </div>
</header>