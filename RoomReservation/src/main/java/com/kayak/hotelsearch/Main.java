package com.kayak.hotelsearch;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayak.hotelsearch.booking.BookingRequest;
import com.kayak.hotelsearch.room.Room;
import com.kayak.hotelsearch.room.RoomDatabaseAccessService;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final int THREAD_POOL_SIZE = 10;
    private static final BlockingQueue<BookingRequest> requestQueue = new LinkedBlockingQueue<>();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static final CountDownLatch completionLatch = new CountDownLatch(1);
    private static volatile boolean isShuttingDown = false;

    public static void main(String[] args) {
        try {
            // Output name to stderr
            System.err.println("Soumya Tejaswi Vadlamani");
            
            // This position (Java Search Engineer) requires presence in our Cambridge or Concord MA office at least 3 days a week. Output your preferred office.
            // Output preferred office to stdout
            System.out.println("Cambridge MA office");

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                isShuttingDown = true;
                shutdownThreadPool();
            }));

            // Start request processor threads
            startRequestProcessors();

            // Read and process booking requests
            readBookingRequests("src/main/resources/booking_requests.json");

            // Wait for all requests to be processed
            if (!completionLatch.await(30, TimeUnit.SECONDS)) {
                LOGGER.warning("Timeout waiting for requests to complete");
            }

            // Print statistics
            printStatistics();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in main execution", e);
        } finally {
            shutdownThreadPool();
        }
    }

    private static void startRequestProcessors() {
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executorService.submit(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted() && !isShuttingDown) {
                        BookingRequest request = requestQueue.poll(100, TimeUnit.MILLISECONDS);
                        if (request == null) {
                            continue;
                        }
                        
                        processBookingRequest(request);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error processing request", e);
                }
            });
        }
    }

    private static void processBookingRequest(BookingRequest request) {
        try {
            LOGGER.info("Processing request for room " + request.getRoomNumber() + " by " + request.getGuest());
            boolean booked = bookRoom(request);
            if (booked) {
                LOGGER.info("Successfully booked room " + request.getRoomNumber() + " for " + request.getGuest());
            } else {
                LOGGER.info("Failed to book room " + request.getRoomNumber() + " for " + request.getGuest());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing booking request", e);
        }
    }

    private static boolean bookRoom(BookingRequest request) {
        RoomDatabaseAccessService service = RoomDatabaseAccessService.getInstance();
        Room room = service.loadRoom(request.getRoomNumber());
        if (room != null && room.isAvailable()) {
            Room bookedRoom = room.bookRoom(request.getGuestName());
            if (bookedRoom != null) {
                service.updateRoom(bookedRoom);
                service.recordSuccessfulBooking();
                return true;
            }
        }
        return false;
    }

    public static void readBookingRequests(String filename) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<BookingRequest> requests = mapper.readValue(new File(filename), new TypeReference<List<BookingRequest>>() {
            });

            for (BookingRequest request : requests) {
                if (isShuttingDown) {
                    break;
                }
                try {
                    requestQueue.put(request);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // Signal that all requests have been queued
            completionLatch.countDown();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading booking requests", e);
        }
    }

    private static void printStatistics() {
        try {
            RoomDatabaseAccessService service = RoomDatabaseAccessService.getInstance();
            printBookingStatistics(service);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error printing statistics", e);
        }
    }

    private static void printBookingStatistics(RoomDatabaseAccessService service) {
        LOGGER.info("\nBooking Statistics:");
        LOGGER.info("Total Booking Attempts: " + service.getSuccessfulBookings());
        LOGGER.info("Successful Bookings: " + service.getSuccessfulBookings());
        LOGGER.info("Booking Success Rate: " + String.format("%.2f%%", service.getBookingSuccessRate() * 100));
        
        LOGGER.info("\nRoom Statistics:");
        for (Room room : service.getAllRooms()) {
            LOGGER.info(String.format("Room %d: %s, Price: $%.2f, Available: %s, Bookings: %d",
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getPrice(),
                    room.isAvailable() ? "Yes" : "No",
                    room.getBookingCount()));
        }
    }

    private static void shutdownThreadPool() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
