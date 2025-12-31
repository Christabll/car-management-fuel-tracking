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

All REST endpoints return responses wrapped in an `ApiResponse` object with the following structure:
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { ... },
  "errors": [],
  "meta": {
    "timestamp": "2024-01-01T12:00:00Z",
    "version": "v1"
  }
}
```

#### Create a Car
- **Endpoint**: `POST /api/cars`
- **Request Body**:
  ```json
  {
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2018
  }
  ```
- **Response** (201 Created):
  ```json
  {
    "success": true,
    "message": "Car created successfully",
    "data": {
      "id": 1,
      "brand": "Toyota",
      "model": "Corolla",
      "year": 2018,
      "fuelEntries": []
    }
  }
  ```

#### List All Cars
- **Endpoint**: `GET /api/cars`
- **Response** (200 OK):
  ```json
  {
    "success": true,
    "message": "Cars retrieved successfully",
    "data": [
      {
        "id": 1,
        "brand": "Toyota",
        "model": "Corolla",
        "year": 2018,
        "fuelEntries": [...]
      }
    ]
  }
  ```

#### Get Car by ID
- **Endpoint**: `GET /api/cars/{id}`
- **Response** (200 OK):
  ```json
  {
    "success": true,
    "message": "Car retrieved successfully",
    "data": {
      "id": 1,
      "brand": "Toyota",
      "model": "Corolla",
      "year": 2018,
      "fuelEntries": [...]
    }
  }
  ```
- **Error Response** (404 Not Found):
  ```json
  {
    "success": false,
    "message": "Car with ID 999 not found",
    "errors": ["Car with ID 999 not found"]
  }
  ```

#### Update a Car
- **Endpoint**: `PUT /api/cars/{id}`
- **Request Body**:
  ```json
  {
    "brand": "Toyota",
    "model": "Camry",
    "year": 2020
  }
  ```
- **Response** (200 OK):
  ```json
  {
    "success": true,
    "message": "Car updated successfully",
    "data": {
      "id": 1,
      "brand": "Toyota",
      "model": "Camry",
      "year": 2020,
      "fuelEntries": [...]
    }
  }
  ```

#### Delete a Car
- **Endpoint**: `DELETE /api/cars/{id}`
- **Response** (200 OK):
  ```json
  {
    "success": true,
    "message": "Car deleted successfully",
    "data": null
  }
  ```

#### Add Fuel Entry
- **Endpoint**: `POST /api/cars/{id}/fuel`
- **Request Body**:
  ```json
  {
    "liters": 40.0,
    "price": 52.5,
    "odometer": 45000
  }
  ```
- **Validation Rules**:
  - `liters`: Required, must be positive
  - `price`: Required, must be positive
  - `odometer`: Required, must be non-negative, must be greater than or equal to previous maximum
- **Response** (201 Created):
  ```json
  {
    "success": true,
    "message": "Fuel entry added successfully",
    "data": {
      "id": 1,
      "liters": 40.0,
      "price": 52.5,
      "odometer": 45000
    }
  }
  ```

#### Get Fuel Statistics
- **Endpoint**: `GET /api/cars/{id}/fuel/stats`
- **Response** (200 OK):
  ```json
  {
    "success": true,
    "message": "Fuel statistics retrieved successfully",
    "data": {
      "totalFuel": 120.0,
      "totalCost": 155.0,
      "averageConsumption": 6.4
    }
  }
  ```
- **Note**: `averageConsumption` is calculated in L/100km and requires at least 2 fuel entries with valid odometer readings.

### Servlet Endpoint

- **Endpoint**: `GET /servlet/fuel-stats?carId={id}`
- **Description**: Manual servlet implementation demonstrating HTTP request lifecycle
- **Response**: Same JSON format as REST API endpoint
- **Example**: `GET /servlet/fuel-stats?carId=1`

### Error Responses

All endpoints return consistent error responses:

**400 Bad Request** (Validation Error):
```json
{
  "success": false,
  "message": "Validation failed: Brand cannot be blank",
  "errors": ["Validation failed: Brand cannot be blank"]
}
```

**404 Not Found**:
```json
{
  "success": false,
  "message": "Car with ID 999 not found",
  "errors": ["Car with ID 999 not found"]
}
```

**409 Conflict** (Duplicate Car):
```json
{
  "success": false,
  "message": "Car with brand 'Toyota', model 'Corolla', and year 2018 already exists",
  "errors": ["Car with brand 'Toyota', model 'Corolla', and year 2018 already exists"]
}
```

**500 Internal Server Error**:
```json
{
  "success": false,
  "message": "An unexpected error occurred",
  "errors": ["An unexpected error occurred"]
}
```

## Key Features

1. **REST API**: Spring Boot controllers with proper HTTP status codes and error handling
2. **Manual Servlet**: Demonstrates understanding of HTTP request lifecycle without framework abstraction
3. **Service Layer**: Shared business logic between REST API and Servlet
4. **CLI Client**: Pure Java HTTP client demonstrating client-server communication
5. **Error Handling**: Proper 404 responses for invalid car IDs
6. **In-Memory Storage**: Thread-safe storage using ConcurrentHashMap

## Testing the API

You can test the API using `curl` or any HTTP client:

### Example: Create a Car
```bash
curl -X POST http://localhost:8080/api/cars \
  -H "Content-Type: application/json" \
  -d '{"brand":"Toyota","model":"Corolla","year":2018}'
```

### Example: Add Fuel Entry
```bash
curl -X POST http://localhost:8080/api/cars/1/fuel \
  -H "Content-Type: application/json" \
  -d '{"liters":40.0,"price":52.5,"odometer":45000}'
```

### Example: Get Fuel Statistics
```bash
curl http://localhost:8080/api/cars/1/fuel/stats
```

### Example: Test Servlet Endpoint
```bash
curl http://localhost:8080/servlet/fuel-stats?carId=1
```

## Validation Rules

### Car Creation/Update
- **brand**: Required, cannot be blank
- **model**: Required, cannot be blank
- **year**: Required, must be between 1886 and (current year + 1)

### Fuel Entry
- **liters**: Required, must be a positive number
- **price**: Required, must be a positive number
- **odometer**: Required, must be non-negative, must be greater than or equal to previous maximum odometer reading

## Technical Highlights

- **Multi-module Maven project**: Demonstrates project organization
- **Service layer reuse**: Same service used by REST and Servlet
- **Manual servlet registration**: Shows how Spring Boot registers servlets
- **Pure Java CLI**: No framework dependencies in client module
- **Proper HTTP semantics**: Correct status codes and error responses
- **Defensive programming**: Defensive copying, null safety, unmodifiable collections
- **Thread-safe storage**: ConcurrentHashMap for in-memory storage
- **Comprehensive error handling**: Global exception handler with proper HTTP status codes
- **API documentation**: OpenAPI/Scalar UI integration
