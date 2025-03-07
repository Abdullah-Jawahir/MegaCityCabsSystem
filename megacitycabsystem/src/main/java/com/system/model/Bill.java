package com.system.model;

public class Bill {
    private String billId;
    private Booking booking;
    private float baseAmount;
    private float taxAmount;
    private float discountAmount;
    private float totalAmount;
    private String status;
    private User generatedBy;
    private String paymentType; 

    public Bill(String billId, Booking booking, float baseAmount, float taxAmount, float discountAmount, float totalAmount, String status, User generatedBy, String paymentType) {
        this.billId = billId;
        this.booking = booking;
        this.baseAmount = baseAmount;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.generatedBy = generatedBy;
        this.paymentType = paymentType;
    }

    
    public Bill(String billId, Booking booking, float baseAmount, float taxAmount, float discountAmount, float totalAmount, String status, User generatedBy) {
        this.billId = billId;
        this.booking = booking;
        this.baseAmount = baseAmount;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.generatedBy = generatedBy;
    }


    // Getters and setters for all fields, including discountAmount and paymentType

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

    public float getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}