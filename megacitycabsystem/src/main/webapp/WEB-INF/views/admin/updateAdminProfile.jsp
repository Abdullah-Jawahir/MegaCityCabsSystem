<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Admin Profile</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&display=swap');

        :root {
            --primary-bg: #0f172a;
            --secondary-bg: #1e293b;
            --accent-blue: #2563eb;
            --accent-green: #10b981;
            --accent-purple: #8b5cf6;
            --accent-orange: #f59e0b;
            --star-color: #FFD700;
            --text-primary: #f8fafc;
            --text-secondary: #94a3b8;
            --border-color: #334155;
            --card-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
            --input-bg: #283549;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background-color: var(--primary-bg);
            color: var(--text-primary);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .container {
            width: 100%;
            max-width: 700px;
            padding: 20px;
        }

        .update-profile-card {
            background-color: var(--secondary-bg);
            border-radius: 16px;
            box-shadow: var(--card-shadow);
            overflow: hidden;
        }

        .card-header {
            background-color: var(--accent-blue);
            padding: 30px;
            position: relative;
            text-align: center;
        }

        .card-header h1 {
            color: var(--text-primary);
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .card-header p {
            color: rgba(255, 255, 255, 0.8);
            font-size: 14px;
        }

        .profile-avatar-container {
            position: relative;
            width: 120px;
            height: 120px;
            margin: 0 auto;
            margin-bottom: 20px;
        }

        .profile-avatar {
            width: 100%;
            height: 100%;
            background-color: #1a365d;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 4px solid rgba(255, 255, 255, 0.2);
        }

        .profile-avatar i {
            font-size: 48px;
            color: #90cdf4;
        }

        .edit-avatar {
            position: absolute;
            bottom: 5px;
            right: 5px;
            background-color: #FFD700;
            color: #000000;
            width: 36px;
            height: 36px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
            border: 2px solid white;
        }

        .edit-avatar i {
            font-size: 16px;
        }

        .card-body {
            padding: 30px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group.full-width {
            grid-column: span 2;
        }

        .form-label {
            display: block;
            margin-bottom: 8px;
            color: var(--text-secondary);
            font-size: 14px;
            font-weight: 500;
        }

        .form-control {
            width: 100%;
            padding: 12px 16px;
            background-color: var(--input-bg);
            border: 1px solid var(--border-color);
            border-radius: 8px;
            color: var(--text-primary);
            font-size: 15px;
            font-family: 'Inter', sans-serif;
            transition: all 0.2s;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--accent-blue);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.3);
        }

        .input-group {
            position: relative;
        }

        .input-icon {
            position: absolute;
            top: 50%;
            left: 16px;
            transform: translateY(-50%);
            color: var(--text-secondary);
        }

        .input-with-icon {
            padding-left: 42px;
        }

        .card-footer {
            padding: 20px 30px;
            background-color: rgba(15, 23, 42, 0.5);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        #error-message {
        	background-color: #f8d7da; 
        	color: #721c24; 
        	border: 1px solid #f5c6cb; 
        	border-radius: 8px; 
        	padding: 15px; 
        	margin: 20px auto 0 30px;
        	text-align: center;
        	width: fit-content;
        }

        .btn {
            display: inline-block;
            padding: 12px 24px;
            border-radius: 8px;
            font-size: 15px;
            font-weight: 500;
            text-decoration: none;
            cursor: pointer;
            border: none;
            transition: all 0.2s;
        }

        .btn-primary {
            background-color: var(--accent-blue);
            color: white;
        }

        .btn-primary:hover {
            background-color: #1d4ed8;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(37, 99, 235, 0.4);
        }

        .btn-secondary {
            background-color: transparent;
            color: var(--text-secondary);
            border: 1px solid var(--border-color);
        }

        .btn-secondary:hover {
            background-color: rgba(255, 255, 255, 0.05);
        }

        @media (max-width: 768px) {
            .form-grid {
                grid-template-columns: 1fr;
            }
            
            .form-group.full-width {
                grid-column: span 1;
            }
            
            .card-footer {
                flex-direction: column;
                gap: 10px;
            }
            
            .btn {
                width: 100%;
                text-align: center;
            }
        }
    </style>
    <script>
        function hideErrorMessage() {
            var errorMessage = document.getElementById('error-message');
            if (errorMessage) {
                setTimeout(function() {
                    errorMessage.style.display = 'none';
                }, 5000);
            }
        }

        // Call the function when the page loads
        window.onload = function() {
            hideErrorMessage();
        };
    </script>
</head>
<body>
    <div class="container">
        <form action="admin?action=updateAdminProfile" method="post" class="update-profile-card">
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

            <c:if test="${not empty errorMessage}">
                <div id="error-message">
                    ${errorMessage}
                </div>
            </c:if>
            
            <div class="card-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label for="name" class="form-label">Full Name</label>
                        <div class="input-group">
                            <i class="fas fa-user input-icon"></i>
                            <input type="text" id="name" name="name" class="form-control input-with-icon" value="${adminUser.name}" required>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="username" class="form-label">Username</label>
                        <div class="input-group">
                            <i class="fas fa-id-badge input-icon"></i>
                            <input type="text" id="username" name="username" class="form-control input-with-icon" value="${adminUser.username}" required>
                        </div>
                    </div>
                    
                    <div class="form-group full-width">
                        <label for="email" class="form-label">Email Address</label>
                        <div class="input-group">
                            <i class="fas fa-envelope input-icon"></i>
                            <input type="email" id="email" name="email" class="form-control input-with-icon" value="${adminUser.email}" required>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="phone" class="form-label">Phone Number</label>
                        <div class="input-group">
                            <i class="fas fa-phone input-icon"></i>
                            <input type="tel" id="phone" name="phone" class="form-control input-with-icon" value="${adminUser.phone}" required>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="role" class="form-label">Role</label>
                        <div class="input-group">
                            <i class="fas fa-user-cog input-icon"></i>
                            <input type="text" id="role" name="role" class="form-control input-with-icon" value="Administrator" readonly>
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
                <a href="admin?action=viewAdminProfile" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>
</body>
</html>