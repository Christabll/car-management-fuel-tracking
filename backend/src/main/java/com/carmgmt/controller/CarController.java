package com.carmgmt.controller;

import com.carmgmt.dto.AddFuelRequest;
import com.carmgmt.dto.ApiResponse;
import com.carmgmt.dto.CreateCarRequest;
import com.carmgmt.model.Car;
import com.carmgmt.model.FuelEntry;
import com.carmgmt.model.FuelStats;
import com.carmgmt.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for car and fuel management endpoints
 */
@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Create a new car
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Car>> createCar(@Valid @RequestBody CreateCarRequest request) {
        Car car = carService.createCar(request.getBrand(), request.getModel(), request.getYear());
        ApiResponse<Car> response = ApiResponse.success("Car created successfully", car);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all cars
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Car>>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        ApiResponse<List<Car>> response = ApiResponse.success("Cars retrieved successfully", cars);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a car by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Car>> getCarById(@PathVariable("id") Long id) {
        Car car = carService.getCarById(id);
        ApiResponse<Car> response = ApiResponse.success("Car retrieved successfully", car);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing car
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Car>> updateCar(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateCarRequest request) {
        Car car = carService.updateCar(id, request.getBrand(), request.getModel(), request.getYear());
        ApiResponse<Car> response = ApiResponse.success("Car updated successfully", car);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a car by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable("id") Long id) {
        carService.deleteCar(id);
        ApiResponse<Void> response = ApiResponse.success("Car deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    /**
     * Add fuel entry to a car
     */
    @PostMapping("/{id}/fuel")
    public ResponseEntity<ApiResponse<FuelEntry>> addFuelEntry(
            @PathVariable("id") Long id,
            @Valid @RequestBody AddFuelRequest request) {
        FuelEntry fuelEntry = carService.addFuelEntry(
                id, 
                request.getLiters(), 
                request.getPrice() != null ? request.getPrice().doubleValue() : null, 
                request.getOdometer()
        );
        ApiResponse<FuelEntry> response = ApiResponse.success("Fuel entry added successfully", fuelEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get fuel statistics for a car
     */
    @GetMapping("/{id}/fuel/stats")
    public ResponseEntity<ApiResponse<FuelStats>> getFuelStats(
            @PathVariable("id") Long id) {
        FuelStats stats = carService.getFuelStats(id);
        ApiResponse<FuelStats> response = ApiResponse.success("Fuel statistics retrieved successfully", stats);
        return ResponseEntity.ok(response);
    }
}

