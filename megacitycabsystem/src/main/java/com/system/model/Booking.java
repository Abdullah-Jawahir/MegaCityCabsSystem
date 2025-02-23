package com.system.model;

import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private LocalDateTime bookingTime;
    private String pickupLocation;
    private String destination;
    private float distance;
    private String status;
    private Driver assignedDriver;
    private Vehicle assignedVehicle;
    private Customer customer;

    public Booking(String bookingId, LocalDateTime bookingTime, String pickupLocation, String destination, float distance, String status, Driver assignedDriver, Vehicle assignedVehicle, Customer customer) {
        this.setBookingId(bookingId);
        this.setBookingTime(bookingTime);
        this.setPickupLocation(pickupLocation);
        this.setDestination(destination);
        this.setDistance(distance);
        this.setStatus(status);
        this.setAssignedDriver(assignedDriver);
        this.setAssignedVehicle(assignedVehicle);
        this.setCustomer(customer);
    }

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public LocalDateTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(LocalDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Driver getAssignedDriver() {
		return assignedDriver;
	}

	public void setAssignedDriver(Driver assignedDriver) {
		this.assignedDriver = assignedDriver;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Vehicle getAssignedVehicle() {
		return assignedVehicle;
	}

	public void setAssignedVehicle(Vehicle assignedVehicle) {
		this.assignedVehicle = assignedVehicle;
	}

    
}

