package com.system.controller;

import com.system.model.Customer;
import com.system.model.User;
import com.system.service.CustomerService;
import com.system.service.RegistrationService;
import com.system.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CustomerService customerService;
    private UserService userService;
    private RegistrationService registrationService;

    @Override
    public void init() throws ServletException {
        customerService = new CustomerService();
        userService = new UserService();
        registrationService = new RegistrationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("manageCustomers")) {
                List<Customer> customers = customerService.getAllCustomers();
                request.setAttribute("customers", customers);
                request.getRequestDispatcher("/WEB-INF/views/admin/manageCustomers.jsp").forward(request, response);

            } else if (action.equals("editCustomer")) {
                int customerId = Integer.parseInt(request.getParameter("customerId"));
                Customer customer = customerService.getCustomerById(customerId);

                if (customer != null) {
                    request.setAttribute("customer", customer);
                    request.getRequestDispatcher("/WEB-INF/views/admin/customers/editCustomer.jsp").forward(request, response);
                } else {
                    response.sendRedirect("customer?error=Customer not found");
                }

            } else if (action.equals("deleteCustomer")) {
                try {
                    int customerId = Integer.parseInt(request.getParameter("customerId"));
                    Customer customer = customerService.getCustomerById(customerId);
                    
                    if (customer != null) {
                        // Get the associated user ID
                        int userId = customer.getUser().getId();
                        
                        // Delete the user (which will cascade delete the customer)
                        boolean userDeleted = userService.deleteUser(userId);
                        
                        if (userDeleted) {
                            response.sendRedirect("customer?success=Customer deleted successfully");
                        } else {
                            response.sendRedirect("customer?error=Unable to delete customer");
                        }
                    } else {
                        response.sendRedirect("customer?error=Customer not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect("customer?error=Invalid customer ID");
                } catch (Exception e) {
                    response.sendRedirect("customer?error=Error deleting customer: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/manageCustomers.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            // Inside the "registerCustomer" branch in doPost:
        	if ("registerCustomer".equals(action)) {
                try {
                    String name = request.getParameter("name");
                    String password = request.getParameter("password");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String address = request.getParameter("address");
                    String nic = request.getParameter("nic");

                    // Preserve entered data
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    request.setAttribute("phone", phone);
                    request.setAttribute("address", address);
                    request.setAttribute("nic", nic);

                    // Field validation
                    if (name == null || name.trim().isEmpty()) {
                        request.setAttribute("error", "Name is required.");
                        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                        return;
                    }
                    if (password == null || password.length() < 6) {
                        request.setAttribute("error", "Password must be at least 6 characters long.");
                        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                        return;
                    }
                    if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        request.setAttribute("error", "Invalid email format.");
                        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                        return;
                    }
                    if (phone == null || phone.trim().isEmpty()) {
                        request.setAttribute("error", "Phone number is required.");
                        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                        return;
                    }
                    
                    if (address == null || address.trim().isEmpty()) {
                        request.setAttribute("error", "Address is required.");
                        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                        return;
                    }
                    if (nic == null || nic.trim().isEmpty()) {
                        request.setAttribute("error", "NIC is required.");
                        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                        return;
                    }

                    // Generate registration number
                    String registrationNumber = customerService.generateNextRegistrationNumber();

                    // Create user object
                    User user = new User(name, password, "customer", email, phone, LocalDateTime.now());

                    // Register customer
                    boolean registrationSuccess = registrationService.registerCustomer(user, registrationNumber, address, nic);

                    if (registrationSuccess) {
                        request.setAttribute("success", "Registration successful! Please login.");
                        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Failed to create customer account. Please try again.");
                        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                    }

                } catch (Exception e) {
                    request.setAttribute("error", e.getMessage()); // Show specific error
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                }
            } else if ("updateCustomer".equals(action)) {
                int customerId = Integer.parseInt(request.getParameter("customerId"));
                String registrationNumber = request.getParameter("registrationNumber");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String nic = request.getParameter("nic");

                Customer customer = customerService.getCustomerById(customerId);

                if (customer != null) {
                    User user = customer.getUser();
                    if (user != null) {
                    	user.setName(name);
                    	user.setPhone(phone);
                        user.setEmail(email);
                        boolean userUpdated = userService.updateUser(user);

                        customer.setRegistrationNumber(registrationNumber);
                        customer.setAddress(address);
                        customer.setNic(nic);
                        boolean customerUpdated = customerService.updateCustomer(customer);

                        if (userUpdated && customerUpdated) {
                            response.sendRedirect("customer");
                        } else {
                            request.setAttribute("error", "Error updating customer or user.");
                            request.getRequestDispatcher("/WEB-INF/views/admin/editCustomer.jsp").forward(request, response);
                        }
                    }
                } else {
                    response.sendRedirect("customer?error=Customer not found");
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/manageCustomers.jsp").forward(request, response);
        }
    }

}
