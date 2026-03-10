# Hotel Reservation Service
## Overview

- The Hotel Reservation Service is a Spring Boot REST application that allows users to create and manage hotel room reservations. The application demonstrates a layered architecture and integrates with external services for payment processing.
- It supports multiple payment methods, including Credit Card and Bank Transfer, and uses Redis messaging for asynchronous payment updates.
- This project showcases common backend development concepts such as REST API design, service layer architecture, exception handling, and scheduled background processing.

# Technologies Used
- Java 17
- Spring Boot
- Spring Web
- Redis
- Maven
- REST APIs
- Logback (Logging)

# Architecture
The application follows a layered architecture:
Controller → Service → Repository → Data / External Services
Layers

## Controller
Handles incoming HTTP requests
Validates request data
Delegates business logic to the service layer

## Service
Contains core business logic
Processes reservation requests
Integrates with payment services

## Repository
Manages reservation data
Client
Communicates with external services such as credit card validation

## Project Structure
src/main/java/com/cgi/hotel/reservation

- controller
- service
- repository
- client
- domain
- exception
- config
- listener
- scheduler

# Key Features
- Create Reservation
- Allows users to create hotel reservations by providing guest details, room type, and payment method.
- Payment Processing
- Supports multiple payment methods:
- Credit Card – Validated through an external service
- Bank Transfer – Processed asynchronously using Redis messaging
- Reservation Status Tracking
- Reservations maintain status updates throughout their lifecycle.

# Example statuses:

- CREATED
- PENDING_PAYMENT
- CONFIRMED
- FAILED

# Global Exception Handling
Centralized error handling ensures consistent API responses.

# Scheduled Tasks
Background jobs are executed using Spring's scheduling mechanism for reservation management.

# API Example
 - Create Reservation

POST
/reservations
Request
{
  "guestName": "John Doe",
  "roomSegment": "DELUXE",
  "paymentMode": "CREDIT_CARD"
}
Response
{
  "reservationId": "12345",
  "status": "CONFIRMED"
}

# Running the Application
1)  Clone the repository
git clone https://github.com/debchakr/Assignment02_HotelReservation.git
2)  Navigate to the project directory
cd Assignment02_HotelReservation
3) Build the project
mvn clean install
4) Run the application
mvn spring-boot:run

# Track and save open api spec
	1) Start the Application
       cd C:\Workspace\Assignment02_HotelReservation\assignment-02-hotel-reservation
      mvn spring-boot:run
       Wait for “ReservationApplication started successfully” in the logs.
	2) Generate the OpenAPI Spec
         In a second terminal, fetch the spec and write it to src/main/resources/openapi.json:
             cd C:\Workspace\Assignment02_HotelReservation\assignment-02-hotel-reservation
                   Invoke-WebRequest -Uri http://localhost:8080/v3/api-docs `
                   -OutFile src\main\resources\openapi.json
	3) Verify the file contents if needed:
              Get-Content src\main\resources\openapi.json | Select-String "openapi"
	4) Stop the Application
	5) Go back to the terminal running mvn spring-boot:run and press Ctrl+C.
	6) Commit the Spec


# Configuration

Application configuration is located in:
- src/main/resources/application.yaml

It contains settings for:
- Server configuration
- Redis connection
- External service endpoints
- Logging configuration
