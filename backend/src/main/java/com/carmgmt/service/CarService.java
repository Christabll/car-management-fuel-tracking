package com.carmgmt.service;

import com.carmgmt.model.Car;
import com.carmgmt.model.FuelEntry;
import com.carmgmt.model.FuelStats;
import com.carmgmt.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service layer for car and fuel operations
 */
@Service
public class CarService {
    private final CarRepository carRepository;
    private final AtomicLong fuelEntryIdGenerator = new AtomicLong(1);

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Create a new car
     */
    public Car createCar(String brand, String model, Integer year) {
        Car car = new Car(brand, model, year);
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
        return carRepository.findById(id);
    }

    /**
     * Add fuel entry to a car
     */
    public FuelEntry addFuelEntry(Long carId, Double liters, Double price, Integer odometer) {
        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new IllegalArgumentException("Car with ID " + carId + " not found");
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
            throw new IllegalArgumentException("Car with ID " + carId + " not found");
        }

        List<FuelEntry> fuelEntries = car.getFuelEntries();
        
        if (fuelEntries.isEmpty()) {
            return new FuelStats(0.0, 0.0, 0.0);
        }

        Double totalFuel = fuelEntries.stream()
                .mapToDouble(FuelEntry::getLiters)
                .sum();

        Double totalCost = fuelEntries.stream()
                .mapToDouble(FuelEntry::getPrice)
                .sum();

        Double averageConsumption = 0.0;
        if (fuelEntries.size() >= 2) {
            List<FuelEntry> sortedEntries = fuelEntries.stream()
                    .sorted((e1, e2) -> Integer.compare(e1.getOdometer(), e2.getOdometer()))
                    .toList();

            Integer totalDistance = sortedEntries.get(sortedEntries.size() - 1).getOdometer() 
                    - sortedEntries.get(0).getOdometer();

            if (totalDistance > 0) {
                averageConsumption = (totalFuel / totalDistance) * 100;
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
}

