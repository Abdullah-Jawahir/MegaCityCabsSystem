package com.system.observer;

import com.system.model.Booking;

public interface BookingObserver {
    void onBookingConfirmed(Booking booking, String billId);
}
