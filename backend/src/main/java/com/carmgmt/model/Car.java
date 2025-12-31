package com.carmgmt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Car model
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private List<FuelEntry> fuelEntries;

    public Car() {
        this.fuelEntries = new ArrayList<>();
    }

    public Car(String brand, String model, Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.fuelEntries = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<FuelEntry> getFuelEntries() {
        if (fuelEntries == null) {
            fuelEntries = new ArrayList<>();
        }
        return Collections.unmodifiableList(fuelEntries);
    }

    public void setFuelEntries(List<FuelEntry> fuelEntries) {
        this.fuelEntries = fuelEntries == null ? new ArrayList<>() : new ArrayList<>(fuelEntries);
    }

    public void addFuelEntry(FuelEntry fuelEntry) {
        if (fuelEntry == null) {
            throw new IllegalArgumentException("FuelEntry cannot be null");
        }
        if (fuelEntries == null) {
            fuelEntries = new ArrayList<>();
        }
        this.fuelEntries.add(fuelEntry);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id) &&
               Objects.equals(brand, car.brand) &&
               Objects.equals(model, car.model) &&
               Objects.equals(year, car.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, year);
    }

    @Override
    public String toString() {
        return "Car{" +
               "id=" + id +
               ", brand='" + brand + '\'' +
               ", model='" + model + '\'' +
               ", year=" + year +
               ", fuelEntriesCount=" + (fuelEntries != null ? fuelEntries.size() : 0) +
               '}';
    }
}

