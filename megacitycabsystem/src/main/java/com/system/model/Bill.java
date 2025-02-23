package com.system.model;

public class Bill {
    private String billId;
    private Booking booking;
    private float baseAmount;
    private float taxAmount;
    private float totalAmount;
    private String status;
    private User generatedBy;  // Add User field

    public Bill(String billId, Booking booking, float baseAmount, float taxAmount, float totalAmount, String status, User generatedBy) {
        this.setBillId(billId);
        this.setBooking(booking);
        this.setBaseAmount(baseAmount);
        this.setTaxAmount(taxAmount);
        this.setTotalAmount(totalAmount);
        this.setStatus(status);
        this.setGeneratedBy(generatedBy);
    }

    // Getters and Setters
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public float getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(float baseAmount) {
        this.baseAmount = baseAmount;
    }

    public float getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(float taxAmount) {
        this.taxAmount = taxAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(User generatedBy) {
        this.generatedBy = generatedBy;
    }
}
