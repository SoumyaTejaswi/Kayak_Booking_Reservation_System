package com.kayak.hotelsearch.booking;

public class BookingRequest {
    private final int roomNumber;
    private final String guest;

    // Default constructor for Jackson deserialization
    public BookingRequest() {
        this.roomNumber = 0;
        this.guest = "";
    }

    public BookingRequest(int roomNumber, String guest) {
        this.roomNumber = roomNumber;
        this.guest = guest;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getGuest() {
        return guest;
    }

    public String getGuestName() {
        return guest;
    }
}
