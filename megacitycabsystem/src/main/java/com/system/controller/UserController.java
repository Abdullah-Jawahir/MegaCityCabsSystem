package com.system.controller;


import com.system.model.User;
import com.system.service.UserService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || "manageUsers".equals(action)) {
            List<User> users = userService.getAllUsers();
            request.setAttribute("users", users);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/manageUsers.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean isSuccess = userService.deleteUser(userId);
            if (isSuccess) {
                response.sendRedirect("UserController?action=viewUsers");
            } else {
                request.setAttribute("errorMessage", "Failed to delete user.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid user ID.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
