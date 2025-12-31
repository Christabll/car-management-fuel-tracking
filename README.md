# Car Management & Fuel Tracking System

A multi-module Java application demonstrating Spring Boot REST API, manual Servlet implementation, and CLI client communication.

## Project Structure

```
car-management-system/
├── backend/          # Spring Boot REST API + Servlet
└── cli-client/       # Pure Java CLI client
```

## Architecture

- **Backend**: Spring Boot application with REST controllers and a manual HttpServlet
- **CLI Client**: Standalone Java application using `java.net.http.HttpClient`
- **Storage**: In-memory storage using ConcurrentHashMap (no database)

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Building the Project

From the project root directory:

```bash
mvn clean install
```

This will build both modules:
- `backend/target/backend-1.0.0.jar` - Spring Boot executable JAR
- `cli-client/target/cli-client-1.0.0.jar` - CLI client executable JAR

## Running the Backend

**Option 1: Using Maven (from project root)**
```bash
cd backend
mvn spring-boot:run
```

**Option 2: Using JAR file (from project root)**
```bash
java -jar backend/target/backend-1.0.0.jar
```

The server will start on `http://localhost:8080`

**Note:** Make sure you've built the project first using `mvn clean install` from the project root.

## API Documentation (Scalar UI)

Once the backend is running, you can access the interactive API documentation:

- **Scalar UI**: `http://localhost:8080/scalar.html` - Modern, beautiful API documentation interface
- **OpenAPI JSON**: `http://localhost:8080/api-docs` - Raw OpenAPI specification

The Scalar UI provides an interactive interface where you can:
- Browse all available endpoints
- View request/response schemas
- Test endpoints directly from the browser
- See example requests and responses

## Running the CLI Client

**Important:** Make sure the backend is running first on `http://localhost:8080`

**Option 1: Using JAR file (from project root)**
```bash
java -jar cli-client/target/cli-client-1.0.0.jar <command> [options]
```

**Option 2: Using Maven (from project root)**
```bash
cd cli-client
mvn exec:java -Dexec.mainClass="com.carmgmt.cli.CliApplication" -Dexec.args="<command> [options]"
```

**Note:** Make sure you've built the project first using `mvn clean install` from the project root.

## CLI Commands

### 1. Create a Car

```bash
java -jar cli-client/target/cli-client-1.0.0.jar create-car --brand Toyota --model Corolla --year 2018
```

### 2. Add Fuel Entry

```bash
java -jar cli-client/target/cli-client-1.0.0.jar add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000
```

### 3. Get Fuel Statistics

```bash
java -jar cli-client/target/cli-client-1.0.0.jar fuel-stats --carId 1
```

Expected output:
```
Total fuel: 120 L
Total cost: 155.00
Average consumption: 6.4 L/100km
```

## API Endpoints

### REST API

- `POST /api/cars` - Create a car
  ```json
  {
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2018
  }
  ```

- `GET /api/cars` - List all cars

- `POST /api/cars/{id}/fuel` - Add fuel entry
  ```json
  {
    "liters": 40.0,
    "price": 52.5,
    "odometer": 45000
  }
  ```

- `GET /api/cars/{id}/fuel/stats` - Get fuel statistics

### Servlet Endpoint

- `GET /servlet/fuel-stats?carId={id}` - Get fuel statistics via manual servlet

## Key Features

1. **REST API**: Spring Boot controllers with proper HTTP status codes and error handling
2. **Manual Servlet**: Demonstrates understanding of HTTP request lifecycle without framework abstraction
3. **Service Layer**: Shared business logic between REST API and Servlet
4. **CLI Client**: Pure Java HTTP client demonstrating client-server communication
5. **Error Handling**: Proper 404 responses for invalid car IDs
6. **In-Memory Storage**: Thread-safe storage using ConcurrentHashMap

## Technical Highlights

- **Multi-module Maven project**: Demonstrates project organization
- **Service layer reuse**: Same service used by REST and Servlet
- **Manual servlet registration**: Shows how Spring Boot registers servlets
- **Pure Java CLI**: No framework dependencies in client module
- **Proper HTTP semantics**: Correct status codes and error responses
