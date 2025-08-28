# Kayak Hotel Reservation System

A robust, thread-safe hotel room booking system built in Java 21, designed to handle concurrent booking requests while maintaining data integrity and providing comprehensive statistics.

## 🏨 Project Overview

This project implements a real-world hotel room reservation system that demonstrates:
- **Concurrent processing** of multiple booking requests
- **Thread-safe operations** using modern Java concurrency features
- **Immutable data structures** for better reliability
- **Comprehensive logging** and statistics tracking
- **Unit testing** with JUnit 5

## 🚀 Features

### Core Functionality
- **Multi-threaded booking processing** with configurable thread pool
- **Room availability management** with real-time updates
- **Booking request queue** with blocking operations
- **Comprehensive statistics** including success rates and room status
- **Graceful shutdown** handling with proper resource cleanup

### Room Types & Pricing
- **Standard Rooms**: $100/night (Rooms 101-102)
- **Deluxe Rooms**: $200/night (Rooms 103-104)
- **Suite Rooms**: $300/night (Rooms 105-106)
- **Presidential Suite**: $500/night (Room 107)

### Technical Features
- **Immutable Room objects** for thread safety
- **Singleton database service** with concurrent access
- **JSON-based booking requests** for easy configuration
- **Builder pattern** for object construction
- **Comprehensive error handling** and logging

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.8** or higher
- **Git** for version control

## 🛠️ Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <your-repository-url>
   cd Kayak_Assessment
   ```

2. **Navigate to the project directory**
   ```bash
   cd RoomReservation
   ```

3. **Compile and run the application**
   ```bash
   mvn clean compile exec:java
   ```

## 🏃‍♂️ Running the Application

### Basic Execution
```bash
mvn clean compile exec:java
```

### Expected Output
The application will:
1. Display your name and office preference
2. Process 10 booking requests with 2-second intervals
3. Show real-time booking progress
4. Display comprehensive statistics

### Sample Output
```
Soumya Tejaswi Vadlamani
Cambridge MA office
INFO: Processing request for room 101 by Guest 1
INFO: Successfully booked room 101 for Guest 1
...
INFO: Booking Statistics:
INFO: Total Booking Attempts: 7
INFO: Successful Bookings: 7
INFO: Booking Success Rate: 70.00%
```

## 🏗️ Architecture

### Core Components

#### 1. **Main Application (`Main.java`)**
- Orchestrates the entire booking process
- Manages thread pool and request queue
- Handles graceful shutdown
- Provides comprehensive logging

#### 2. **Room Management (`Room.java`)**
- **Immutable** room representation
- Builder pattern for construction
- Thread-safe booking/unbooking operations
- Comprehensive validation

#### 3. **Database Service (`RoomDatabaseAccessService.java`)**
- **Singleton pattern** for global access
- **ConcurrentHashMap** for thread-safe storage
- Atomic counters for statistics
- Room initialization and management

#### 4. **Booking Request (`BookingRequest.java`)**
- Simple data transfer object
- JSON serialization support
- Validation and accessor methods

### Thread Safety Features
- **Immutable Room objects** prevent race conditions
- **ConcurrentHashMap** for thread-safe storage
- **AtomicInteger** for counter operations
- **BlockingQueue** for request processing
- **Synchronized singleton** access

## 📊 Configuration

### Booking Requests (`booking_requests.json`)
```json
[
  {"roomNumber": 101, "guest": "Guest 1"},
  {"roomNumber": 102, "guest": "Guest 2"},
  ...
]
```

### Thread Pool Configuration
- **Default size**: 10 threads
- **Configurable** via `THREAD_POOL_SIZE` constant
- **Graceful shutdown** with 5-second timeout

### Logging Configuration
- **Java Util Logging** framework
- **Configurable** via `logging.properties`
- **Comprehensive** error and info logging

## 🧪 Testing

### Running Tests
```bash
mvn test
```

### Test Coverage
- **RoomDatabaseAccessService** comprehensive unit tests
- **Concurrent access** testing
- **Edge cases** and error conditions
- **Statistics accuracy** validation

## 🔧 Key Improvements Implemented

### 1. **Thread Safety**
- Immutable Room objects
- Concurrent data structures
- Atomic operations for counters

### 2. **Performance Optimization**
- Efficient thread pool management
- Non-blocking queue operations
- Optimized room lookups

### 3. **Error Handling**
- Comprehensive exception handling
- Graceful degradation
- Detailed logging for debugging

### 4. **Code Quality**
- Builder pattern implementation
- Immutable objects where appropriate
- Comprehensive validation
- Clean separation of concerns

## 📈 Performance Characteristics

### Scalability
- **Horizontal scaling** via thread pool configuration
- **Memory efficient** immutable objects
- **Lock-free** operations where possible

### Throughput
- **Concurrent processing** of multiple requests
- **Efficient queue management**
- **Minimal blocking** operations

### Reliability
- **Data integrity** through immutability
- **Graceful error handling**
- **Comprehensive logging**

## 🚨 Important Notes

### Thread Safety
- All room operations are thread-safe
- Database service uses concurrent collections
- Atomic operations prevent race conditions

### Resource Management
- Proper thread pool shutdown
- Memory-efficient immutable objects
- Automatic cleanup of resources

### Error Handling
- Comprehensive exception handling
- Graceful degradation on errors
- Detailed logging for debugging

## 📝 License

This project was created as part of the Kayak coding challenge assessment.

## 👨‍💻 Author

**Soumya Tejaswi Vadlamani**
- **Office**: Cambridge MA office
- **Position**: Java Engineer

---

## 🎯 Challenge Objectives Met

✅ **Section 1**: Name and office preference output  
✅ **Section 2**: Working booking system with proper concurrency  
✅ **Section 3**: Performance optimizations implemented  
✅ **Section 4**: Thread safety and data integrity issues resolved  
✅ **Bonus 1**: Immutable classes implemented where appropriate  
✅ **Bonus 2**: Comprehensive unit tests for RoomDatabaseAccessService  

The system demonstrates production-ready code quality with proper error handling, thread safety, and comprehensive testing.
