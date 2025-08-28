package com.kayak.hotelsearch.room;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomDatabaseAccessService {
    private static RoomDatabaseAccessService instance;
    private final ConcurrentHashMap<Integer, Room> roomCache;
    private final AtomicInteger totalBookings;
    private final AtomicInteger successfulBookings;

    private RoomDatabaseAccessService() {
        roomCache = new ConcurrentHashMap<>();
        totalBookings = new AtomicInteger(0);
        successfulBookings = new AtomicInteger(0);
        initializeRooms();
    }

    public static synchronized RoomDatabaseAccessService getInstance() {
        if (instance == null) {
            instance = new RoomDatabaseAccessService();
        }
        return instance;
    }

    private void initializeRooms() {
        roomCache.put(101, Room.builder()
                .roomNumber(101)
                .roomType(RoomType.STANDARD)
                .price(100.0)
                .isAvailable(true)
                .bookingCount(0)
                .build());
        roomCache.put(102, Room.builder()
                .roomNumber(102)
                .roomType(RoomType.STANDARD)
                .price(100.0)
                .isAvailable(true)
                .bookingCount(0)
                .build());
        roomCache.put(103, Room.builder()
                .roomNumber(103)
                .roomType(RoomType.DELUXE)
                .price(200.0)
                .isAvailable(true)
                .bookingCount(0)
                .build());
        roomCache.put(104, Room.builder()
                .roomNumber(104)
                .roomType(RoomType.DELUXE)
                .price(200.0)
                .isAvailable(true)
                .bookingCount(0)
                .build());
        roomCache.put(105, Room.builder()
                .roomNumber(105)
                .roomType(RoomType.SUITE)
                .price(300.0)
                .isAvailable(true)
                .bookingCount(0)
                .build());
        roomCache.put(106, Room.builder()
                .roomNumber(106)
                .roomType(RoomType.SUITE)
                .price(300.0)
                .isAvailable(true)
                .bookingCount(0)
                .build());
        roomCache.put(107, Room.builder()
                .roomNumber(107)
                .roomType(RoomType.PRESIDENTIAL)
                .price(500.0)
                .isAvailable(true)
                .bookingCount(0)
                .build());
    }

    public Room loadRoom(int roomNumber) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be positive");
        }
        totalBookings.incrementAndGet();
        return roomCache.get(roomNumber);
    }

    public void recordSuccessfulBooking() {
        successfulBookings.incrementAndGet();
    }

    public int getSuccessfulBookings() {
        return successfulBookings.get();
    }

    public double getBookingSuccessRate() {
        int total = totalBookings.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) successfulBookings.get() / total;
    }

    public Collection<Room> getAllRooms() {
        return Collections.unmodifiableCollection(roomCache.values());
    }

    public boolean isRoomAvailable(int roomNumber) {
        Room room = roomCache.get(roomNumber);
        return room != null && room.isAvailable();
    }

    public int getAvailableRoomCount() {
        return (int) roomCache.values().stream()
                .filter(Room::isAvailable)
                .count();
    }

    public void updateRoom(Room room) {
        if (room != null) {
            roomCache.put(room.getRoomNumber(), room);
        }
    }
}
