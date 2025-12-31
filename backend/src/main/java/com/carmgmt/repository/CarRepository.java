package com.carmgmt.repository;

import com.carmgmt.model.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
}

