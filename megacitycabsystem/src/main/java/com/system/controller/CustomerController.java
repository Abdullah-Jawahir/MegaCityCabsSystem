package com.system.controller;

import com.system.model.Customer;
import com.system.model.User;
import com.system.service.CustomerService;
import com.system.service.RegistrationService;
import com.system.service.UserService;
import com.system.utils.PasswordHasher;

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
            else if (action.equals("viewProfile")) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");  // Retrieve user from session

                if (user == null) {
                    // User not logged in, redirect to login page
                    response.sendRedirect("login");
                    return; // Exit the method
                }
                 int userId = user.getId();

                // Retrieve customer details based on the user ID(important)
                Customer customer = customerService.getCustomerByUserId(userId);

                if (customer != null) {
                    request.setAttribute("customer", customer); // pass customer to the JSP
                    request.setAttribute("user", user);       // pass user to the JSP
                    request.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(request, response);  // Changed jsp directory
                }
                else{
                    request.setAttribute("error", "Customer details not found for this user.");
                    request.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(request, response);
                }
            }

            // New action for viewing the update form
            else if (action.equals("viewUpdateCustomer")) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");

                if (user == null) {
                    // User not logged in, redirect to login page
                    response.sendRedirect("login");
                    return;
                }

                int userId = user.getId();
                Customer customer = customerService.getCustomerByUserId(userId);

                if (customer != null) {
                    request.setAttribute("customer", customer); // Pass customer to the JSP
                    request.setAttribute("user", user); // Pass user to the JSP
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response); // Forward to update form
                } else {
                    request.setAttribute("error", "Customer details not found for this user.");
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
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
                    String username = request.getParameter("username"); // get the username
                    String password = request.getParameter("password");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String address = request.getParameter("address");
                    String nic = request.getParameter("nic");

                    // Preserve entered data
                    request.setAttribute("name", name);
                    request.setAttribute("username", username);
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
                    if (username == null || username.trim().isEmpty()) {
                        request.setAttribute("error", "Username is required.");
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
                    User user = new User(name, username, password, "customer", email, phone, LocalDateTime.now());

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
            } else if ("updateCustomerByAdmin".equals(action)) {
                int customerId = Integer.parseInt(request.getParameter("customerId"));
                String registrationNumber = request.getParameter("registrationNumber");
                String name = request.getParameter("name");
                String username = request.getParameter("username"); // Get the username from request
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String nic = request.getParameter("nic");

                Customer customer = customerService.getCustomerById(customerId);

                if (customer != null) {
                    User user = customer.getUser();
                    if (user != null) {
                        user.setName(name);
                        user.setUsername(username);  // set the username
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
            } else if ("updateCustomer".equals(action)) {
                HttpSession session = request.getSession();
                User loggedInUser = (User) session.getAttribute("user");

                if (loggedInUser == null) {
                    response.sendRedirect("login");  //Handle no logged-in user
                    return;
                }

                int customerId = Integer.parseInt(request.getParameter("customerId"));
                String name = request.getParameter("name");
                String username = request.getParameter("username");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String nic = request.getParameter("nic");

                // Preserve entered data to re-populate the form if validation fails
                request.setAttribute("name", name);
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);

                //Fetch from DB (Get these here, to keep the values preserved even if the
                //form has other issues like password matching)
                Customer customer = customerService.getCustomerById(customerId);
                User user = loggedInUser;  // Use session to retrieve user

                //The Customer objects must be non-null
                if (customer == null || user == null) {
                    request.setAttribute("error", "Customer or User details not found.");
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                    return;
                }

                request.setAttribute("address", address);
                request.setAttribute("nic", nic);


                // Validate the required fields
                if (name == null || name.trim().isEmpty()) {
                    request.setAttribute("error", "Name is required.");
                    request.setAttribute("customer", customer);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                    return;
                }

                if (username == null || username.trim().isEmpty()) {
                    request.setAttribute("error", "Username is required.");
                    request.setAttribute("customer", customer);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                    return;
                }

                if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    request.setAttribute("error", "Invalid email format.");
                    request.setAttribute("customer", customer);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                    return;
                }

                if (phone == null || phone.trim().isEmpty()) {
                    request.setAttribute("error", "Phone is required.");
                    request.setAttribute("customer", customer);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                    return;
                }

                if (address == null || address.trim().isEmpty()) {
                    request.setAttribute("error", "Address is required.");
                    request.setAttribute("customer", customer);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                    return;
                }

                if (nic == null || nic.trim().isEmpty()) {
                    request.setAttribute("error", "NIC is required.");
                    request.setAttribute("customer", customer);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                    return;
                }

                //Update the User
                user.setName(name);
                user.setUsername(username);
                user.setEmail(email);
                user.setPhone(phone);

                //If new password field is set, then proceed,
                if (newPassword != null && !newPassword.isEmpty()) {
                    if (newPassword.equals(confirmPassword)) {
                        // Good practice to hash the password before saving
                        String hashedPassword = PasswordHasher.hash(newPassword);
                        user.setPassword(hashedPassword);
                    } else {
                        request.setAttribute("error", "New password and confirm password do not match.");
                        request.setAttribute("customer", customer);
                        request.setAttribute("user", user);
                        request.setAttribute("address", address);
                        request.setAttribute("nic", nic);
                        request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                        return;
                    }
                }

                boolean userUpdated = userService.updateUser(user);

                //Update the Customer
                customer.setAddress(address);
                customer.setNic(nic);

                boolean customerUpdated = customerService.updateCustomer(customer);


                if (userUpdated && customerUpdated) {
                    //update session user object

                    response.sendRedirect("customer?action=viewProfile"); // Redirect to the profile
                } else {
                    request.setAttribute("error", "Failed to update profile. Please try again.");
                    request.setAttribute("customer", customer);
                    request.setAttribute("user", user);
                    request.setAttribute("address", address);
                    request.setAttribute("nic", nic);
                    request.getRequestDispatcher("/WEB-INF/views/customer/updateCustomer.jsp").forward(request, response);
                }
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/manageCustomers.jsp").forward(request, response);
        }
    }

}