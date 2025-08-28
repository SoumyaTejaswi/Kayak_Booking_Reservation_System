package com.kayak.hotelsearch.room;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Room {
    private final int roomNumber;
    private final RoomType roomType;
    private final double price;
    private final boolean isAvailable;
    private final String currentGuest;
    private final LocalDateTime lastBookingTime;
    private final int bookingCount;

    private Room(Builder builder) {
        this.roomNumber = builder.roomNumber;
        this.roomType = builder.roomType;
        this.price = builder.price;
        this.isAvailable = builder.isAvailable;
        this.currentGuest = builder.currentGuest;
        this.lastBookingTime = builder.lastBookingTime;
        this.bookingCount = builder.bookingCount;
    }

    public static class Builder {
        private int roomNumber;
        private RoomType roomType;
        private double price;
        private boolean isAvailable;
        private String currentGuest;
        private LocalDateTime lastBookingTime;
        private int bookingCount;

        public Builder roomNumber(int roomNumber) {
            if (roomNumber <= 0) {
                throw new IllegalArgumentException("Room number must be positive");
            }
            this.roomNumber = roomNumber;
            return this;
        }

        public Builder roomType(RoomType roomType) {
            if (roomType == null) {
                throw new IllegalArgumentException("Room type cannot be null");
            }
            this.roomType = roomType;
            return this;
        }

        public Builder price(double price) {
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            this.price = price;
            return this;
        }

        public Builder isAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public Builder currentGuest(String currentGuest) {
            this.currentGuest = currentGuest;
            return this;
        }

        public Builder lastBookingTime(LocalDateTime lastBookingTime) {
            this.lastBookingTime = lastBookingTime;
            return this;
        }

        public Builder bookingCount(int bookingCount) {
            if (bookingCount < 0) {
                throw new IllegalArgumentException("Booking count cannot be negative");
            }
            this.bookingCount = bookingCount;
            return this;
        }

        public Room build() {
            return new Room(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Room bookRoom(String guest) {
        if (guest == null || guest.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be null or empty");
        }
        if (!isAvailable) {
            return this;
        }
        return new Builder()
                .roomNumber(this.roomNumber)
                .roomType(this.roomType)
                .price(this.price)
                .isAvailable(false)
                .currentGuest(guest)
                .lastBookingTime(LocalDateTime.now())
                .bookingCount(this.bookingCount + 1)
                .build();
    }

    public Room unbookRoom() {
        if (isAvailable) {
            return this;
        }
        return new Builder()
                .roomNumber(this.roomNumber)
                .roomType(this.roomType)
                .price(this.price)
                .isAvailable(true)
                .bookingCount(this.bookingCount)
                .build();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrentGuest() {
        return currentGuest;
    }

    public LocalDateTime getLastBookingTime() {
        return lastBookingTime;
    }

    public int getBookingCount() {
        return bookingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", currentGuest='" + currentGuest + '\'' +
                ", lastBookingTime=" + lastBookingTime +
                ", bookingCount=" + bookingCount +
                '}';
    }
}
