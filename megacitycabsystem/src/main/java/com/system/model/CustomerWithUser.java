package com.system.model;


public class CustomerWithUser {
    private Customer customer;
    private User user;

    // Constructor
    public CustomerWithUser(Customer customer, User user) {
        this.customer = customer;
        this.user = user;
    }

    // Getters
    public Customer getCustomer() {
        return customer;
    }

    public User getUser() {
        return user;
    }
}
