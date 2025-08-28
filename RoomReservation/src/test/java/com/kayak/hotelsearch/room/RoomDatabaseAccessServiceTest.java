package com.kayak.hotelsearch.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RoomDatabaseAccessServiceTest {
    private RoomDatabaseAccessService service;

    @BeforeEach
    void setUp() {
        // Reset the singleton instance before each test
        service = RoomDatabaseAccessService.getInstance();
    }

    @Nested
    @DisplayName("Singleton Tests")
    class SingletonTests {
        @Test
        @DisplayName("Should return same instance on multiple calls")
        void shouldReturnSameInstance() {
            RoomDatabaseAccessService instance1 = RoomDatabaseAccessService.getInstance();
            RoomDatabaseAccessService instance2 = RoomDatabaseAccessService.getInstance();
            assertSame(instance1, instance2, "Should return the same instance");
        }
    }

    @Nested
    @DisplayName("Room Loading Tests")
    class RoomLoadingTests {
        @Test
        @DisplayName("Should load existing room")
        void shouldLoadExistingRoom() {
            Room room = service.loadRoom(101);
            assertNotNull(room, "Room should not be null");
            assertEquals(101, room.getRoomNumber(), "Room number should match");
        }

        @Test
        @DisplayName("Should return null for non-existent room")
        void shouldReturnNullForNonExistentRoom() {
            Room room = service.loadRoom(999);
            assertNull(room, "Room should be null for non-existent room number");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -100})
        @DisplayName("Should throw exception for invalid room numbers")
        void shouldThrowExceptionForInvalidRoomNumbers(int roomNumber) {
            assertThrows(IllegalArgumentException.class, () -> service.loadRoom(roomNumber));
        }
    }

    @Nested
    @DisplayName("Booking Statistics Tests")
    class BookingStatisticsTests {
        @Test
        @DisplayName("Should track successful bookings")
        void shouldTrackSuccessfulBookings() {
            service.recordSuccessfulBooking();
            service.recordSuccessfulBooking();
            assertEquals(2, service.getSuccessfulBookings(), "Should track successful bookings");
        }

        @Test
        @DisplayName("Should calculate correct success rate")
        void shouldCalculateCorrectSuccessRate() {
            service.recordSuccessfulBooking();
            service.recordSuccessfulBooking();
            service.loadRoom(101); // This increments total bookings
            service.loadRoom(102); // This increments total bookings
            assertEquals(0.5, service.getBookingSuccessRate(), "Success rate should be 50%");
        }

        @Test
        @DisplayName("Should return zero success rate for no bookings")
        void shouldReturnZeroSuccessRateForNoBookings() {
            assertEquals(0.0, service.getBookingSuccessRate(), "Success rate should be 0% for no bookings");
        }
    }

    @Nested
    @DisplayName("Room Collection Tests")
    class RoomCollectionTests {
        @Test
        @DisplayName("Should return all rooms")
        void shouldReturnAllRooms() {
            Collection<Room> rooms = service.getAllRooms();
            assertNotNull(rooms, "Room collection should not be null");
            assertFalse(rooms.isEmpty(), "Room collection should not be empty");
            assertEquals(7, rooms.size(), "Should have 7 rooms");
        }

        @Test
        @DisplayName("Should return unmodifiable collection")
        void shouldReturnUnmodifiableCollection() {
            Collection<Room> rooms = service.getAllRooms();
            assertThrows(UnsupportedOperationException.class, () -> rooms.add(null));
        }
    }

    @Nested
    @DisplayName("Concurrent Access Tests")
    class ConcurrentAccessTests {
        @Test
        @DisplayName("Should handle concurrent room loading")
        void shouldHandleConcurrentRoomLoading() throws InterruptedException {
            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            AtomicInteger successCount = new AtomicInteger(0);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        Room room = service.loadRoom(101);
                        if (room != null) {
                            successCount.incrementAndGet();
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();

            assertEquals(threadCount, successCount.get(), "All threads should successfully load the room");
        }

        @Test
        @DisplayName("Should handle concurrent booking tracking")
        void shouldHandleConcurrentBookingTracking() throws InterruptedException {
            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        service.recordSuccessfulBooking();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();

            assertEquals(threadCount, service.getSuccessfulBookings(), "Should track all concurrent bookings");
        }
    }

    @Nested
    @DisplayName("Room Availability Tests")
    class RoomAvailabilityTests {
        @ParameterizedTest
        @CsvSource({
            "101, true",
            "103, false",
            "105, false"
        })
        @DisplayName("Should check room availability correctly")
        void shouldCheckRoomAvailability(int roomNumber, boolean expectedAvailability) {
            assertEquals(expectedAvailability, service.isRoomAvailable(roomNumber));
        }

        @Test
        @DisplayName("Should count available rooms correctly")
        void shouldCountAvailableRooms() {
            int availableCount = service.getAvailableRoomCount();
            assertTrue(availableCount > 0, "Should have some available rooms");
            assertTrue(availableCount <= 7, "Should not have more available rooms than total rooms");
        }
    }
} 