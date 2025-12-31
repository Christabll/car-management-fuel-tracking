package com.carmgmt.service;

import com.carmgmt.exception.CarNotFoundException;
import com.carmgmt.exception.DuplicateCarException;
import com.carmgmt.exception.ValidationException;
import com.carmgmt.model.Car;
import com.carmgmt.model.FuelEntry;
import com.carmgmt.model.FuelStats;
import com.carmgmt.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service layer for car and fuel operations
 */
@Service
public class CarService {
    private static final int FIRST_CAR_YEAR = 1886; // First car was invented in 1886
    private static final int FUTURE_YEAR_ALLOWANCE = 1; // Allow one year in the future for new models
    
    private final CarRepository carRepository;
    private final AtomicLong fuelEntryIdGenerator = new AtomicLong(1);

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Validate car input parameters
     */
    private void validateCarInput(String brand, String model, Integer year) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new ValidationException("Brand cannot be null or empty");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new ValidationException("Model cannot be null or empty");
        }
        if (year == null) {
            throw new ValidationException("Year cannot be null");
        }
        
        int currentYear = Year.now().getValue();
        if (year < FIRST_CAR_YEAR) {
            throw new ValidationException("Year cannot be before " + FIRST_CAR_YEAR + " (first car was invented)");
        }
        if (year > currentYear + FUTURE_YEAR_ALLOWANCE) {
            throw new ValidationException("Year cannot be more than " + FUTURE_YEAR_ALLOWANCE + " year(s) in the future");
        }
    }

    /**
     * Create a new car
     */
    public Car createCar(String brand, String model, Integer year) {
        // Validate input
        validateCarInput(brand, model, year);
        
        // Check for duplicate car
        if (carRepository.existsByBrandModelYear(brand.trim(), model.trim(), year)) {
            throw new DuplicateCarException(
                String.format("Car with brand '%s', model '%s', and year %d already exists", 
                    brand.trim(), model.trim(), year)
            );
        }
        
        Car car = new Car(brand.trim(), model.trim(), year);
        return carRepository.save(car);
    }

    /**
     * Get all cars
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * Find car by ID
     */
    public Car getCarById(Long id) {
        Car car = carRepository.findById(id);
        if (car == null) {
            throw new CarNotFoundException("Car with ID " + id + " not found");
        }
        return car;
    }

    /**
     * Add fuel entry to a car
     */
    public FuelEntry addFuelEntry(Long carId, Double liters, Double price, Integer odometer) {
        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new CarNotFoundException("Car with ID " + carId + " not found");
        }

        // Validate input
        if (liters == null || liters <= 0) {
            throw new ValidationException("Liters must be a positive number");
        }
        if (price == null || price < 0) {
            throw new ValidationException("Price cannot be negative");
        }
        if (odometer == null || odometer < 0) {
            throw new ValidationException("Odometer must be a non-negative number");
        }

        // Validate odometer is increasing
        List<FuelEntry> existingEntries = car.getFuelEntries();
        if (!existingEntries.isEmpty()) {
            Integer maxOdometer = existingEntries.stream()
                    .filter(e -> e.getOdometer() != null)
                    .mapToInt(FuelEntry::getOdometer)
                    .max()
                    .orElse(0);
            
            if (odometer < maxOdometer) {
                throw new ValidationException(
                    String.format("Odometer reading (%d) cannot be less than previous maximum (%d)", 
                        odometer, maxOdometer)
                );
            }
        }

        FuelEntry fuelEntry = new FuelEntry(liters, price, odometer);
        fuelEntry.setId(fuelEntryIdGenerator.getAndIncrement());
        car.addFuelEntry(fuelEntry);
        return fuelEntry;
    }

    /**
     * Calculate fuel statistics for a car
     */
    public FuelStats getFuelStats(Long carId) {
        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new CarNotFoundException("Car with ID " + carId + " not found");
        }

        List<FuelEntry> fuelEntries = car.getFuelEntries();
        
        if (fuelEntries.isEmpty()) {
            return new FuelStats(0.0, 0.0, 0.0);
        }

        Double totalFuel = fuelEntries.stream()
                .filter(e -> e.getLiters() != null)
                .mapToDouble(FuelEntry::getLiters)
                .sum();

        Double totalCost = fuelEntries.stream()
                .filter(e -> e.getPrice() != null)
                .mapToDouble(FuelEntry::getPrice)
                .sum();

        Double averageConsumption = 0.0;
        if (fuelEntries.size() >= 2) {
            List<FuelEntry> sortedEntries = fuelEntries.stream()
                    .filter(e -> e.getOdometer() != null)
                    .sorted((e1, e2) -> Integer.compare(e1.getOdometer(), e2.getOdometer()))
                    .toList();

            if (sortedEntries.size() >= 2) {
                Integer firstOdometer = sortedEntries.get(0).getOdometer();
                Integer lastOdometer = sortedEntries.get(sortedEntries.size() - 1).getOdometer();
                
                if (firstOdometer != null && lastOdometer != null) {
                    Integer totalDistance = lastOdometer - firstOdometer;
                    if (totalDistance > 0) {
                        averageConsumption = (totalFuel / totalDistance) * 100;
                    }
                }
            }
        }

        return new FuelStats(totalFuel, totalCost, averageConsumption);
    }

    /**
     * Check if car exists
     */
    public boolean carExists(Long id) {
        return carRepository.existsById(id);
    }

    /**
     * Update an existing car
     */
    public Car updateCar(Long id, String brand, String model, Integer year) {
        Car existingCar = carRepository.findById(id);
        if (existingCar == null) {
            throw new CarNotFoundException("Car with ID " + id + " not found");
        }

        // Validate input
        validateCarInput(brand, model, year);

        // Update car properties
        existingCar.setBrand(brand.trim());
        existingCar.setModel(model.trim());
        existingCar.setYear(year);
        
        return carRepository.update(existingCar);
    }

    /**
     * Delete a car by ID
     */
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id);
        if (car == null) {
            throw new CarNotFoundException("Car with ID " + id + " not found");
        }
        carRepository.deleteById(id);
    }
}

