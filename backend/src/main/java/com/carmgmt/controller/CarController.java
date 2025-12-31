package com.carmgmt.controller;

import com.carmgmt.model.Car;
import com.carmgmt.model.FuelEntry;
import com.carmgmt.model.FuelStats;
import com.carmgmt.service.CarService;
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
    public ResponseEntity<com.carmgmt.dto.ApiResponse<Car>> createCar(@RequestBody CreateCarRequest request) {
        Car car = carService.createCar(request.getBrand(), request.getModel(), request.getYear());
        com.carmgmt.dto.ApiResponse<Car> response = com.carmgmt.dto.ApiResponse.success("Car created successfully", car);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all cars
     */
    @GetMapping
    public ResponseEntity<com.carmgmt.dto.ApiResponse<List<Car>>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        com.carmgmt.dto.ApiResponse<List<Car>> response = com.carmgmt.dto.ApiResponse.success("Cars retrieved successfully", cars);
        return ResponseEntity.ok(response);
    }

    /**
     * Add fuel entry to a car
     */
    @PostMapping("/{id}/fuel")
    public ResponseEntity<com.carmgmt.dto.ApiResponse<FuelEntry>> addFuelEntry(
            @PathVariable("id") Long id,
            @RequestBody AddFuelRequest request) {
        try {
            FuelEntry fuelEntry = carService.addFuelEntry(
                    id, 
                    request.getLiters(), 
                    request.getPrice(), 
                    request.getOdometer()
            );
            com.carmgmt.dto.ApiResponse<FuelEntry> response = com.carmgmt.dto.ApiResponse.success("Fuel entry added successfully", fuelEntry);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            com.carmgmt.dto.ApiResponse<FuelEntry> response = com.carmgmt.dto.ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            com.carmgmt.dto.ApiResponse<FuelEntry> response = com.carmgmt.dto.ApiResponse.error("Error adding fuel entry: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get fuel statistics for a car
     */
    @GetMapping("/{id}/fuel/stats")
    public ResponseEntity<com.carmgmt.dto.ApiResponse<FuelStats>> getFuelStats(
            @PathVariable("id") Long id) {
        try {
            FuelStats stats = carService.getFuelStats(id);
            com.carmgmt.dto.ApiResponse<FuelStats> response = com.carmgmt.dto.ApiResponse.success("Fuel statistics retrieved successfully", stats);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            com.carmgmt.dto.ApiResponse<FuelStats> response = com.carmgmt.dto.ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            com.carmgmt.dto.ApiResponse<FuelStats> response = com.carmgmt.dto.ApiResponse.error("Error retrieving fuel stats: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Inner classes for request/response DTOs
    static class CreateCarRequest {
        private String brand;
        private String model;
        private Integer year;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }
    }

    static class AddFuelRequest {
        private Double liters;
        private Double price;
        private Integer odometer;

        public Double getLiters() {
            return liters;
        }

        public void setLiters(Double liters) {
            this.liters = liters;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getOdometer() {
            return odometer;
        }

        public void setOdometer(Integer odometer) {
            this.odometer = odometer;
        }
    }

}

