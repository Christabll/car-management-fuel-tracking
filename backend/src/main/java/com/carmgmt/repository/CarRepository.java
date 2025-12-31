package com.carmgmt.repository;

import com.carmgmt.exception.CarNotFoundException;
import com.carmgmt.model.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for cars
 */
@Repository
public class CarRepository {
    private final Map<Long, Car> cars = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Save a car
     */
    public Car save(Car car) {
        Long id = idGenerator.getAndIncrement();
        car.setId(id);
        cars.put(id, car);
        return car;
    }

    /**
     * Find car by ID
     */
    public Car findById(Long id) {
        return cars.get(id);
    }

    /**
     * Get all cars
     */
    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }

    /**
     * Check if car exists
     */
    public boolean existsById(Long id) {
        return cars.containsKey(id);
    }

    /**
     * Check if a car with the same brand, model, and year already exists
     */
    public boolean existsByBrandModelYear(String brand, String model, Integer year) {
        // Validate input parameters are non-null
        if (brand == null || model == null || year == null) {
            return false;
        }
        
        return cars.values().stream()
                .anyMatch(car -> car.getBrand() != null 
                        && car.getModel() != null 
                        && brand.equalsIgnoreCase(car.getBrand()) 
                        && model.equalsIgnoreCase(car.getModel()) 
                        && Objects.equals(year, car.getYear()));
    }

    /**
     * Find a car by brand, model, and year (case-insensitive)
     * Returns Optional.empty() if no matching car is found
     */
    public Optional<Car> findByBrandModelYear(String brand, String model, Integer year) {
        // Validate input parameters are non-null
        if (brand == null || model == null || year == null) {
            return Optional.empty();
        }
        
        return cars.values().stream()
                .filter(car -> car.getBrand() != null 
                        && car.getModel() != null 
                        && brand.equalsIgnoreCase(car.getBrand()) 
                        && model.equalsIgnoreCase(car.getModel()) 
                        && Objects.equals(year, car.getYear()))
                .findFirst();
    }

    /**
     * Update an existing car
     */
    public Car update(Car car) {
        Objects.requireNonNull(car, "Car cannot be null");
        Long carId = Objects.requireNonNull(car.getId(), "Car ID cannot be null");
        
        Car existing = cars.get(carId);
        if (existing == null) {
            throw new CarNotFoundException("Car with ID " + carId + " not found");
        }
        
        // Update the existing car's properties to preserve internal state (e.g., fuelEntries)
        existing.setBrand(car.getBrand());
        existing.setModel(car.getModel());
        existing.setYear(car.getYear());
        
        return existing;
    }

    /**
     * Delete a car by ID
     */
    public boolean deleteById(Long id) {
        return cars.remove(id) != null;
    }
}

