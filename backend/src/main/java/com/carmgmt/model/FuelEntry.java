package com.carmgmt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

/**
 * Fuel entry model
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuelEntry {
    private Long id;
    private Double liters;
    private Double price;
    private Integer odometer;

    public FuelEntry() {
    }

    public FuelEntry(Double liters, Double price, Integer odometer) {
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelEntry fuelEntry = (FuelEntry) o;
        return Objects.equals(id, fuelEntry.id) &&
               Objects.equals(liters, fuelEntry.liters) &&
               Objects.equals(price, fuelEntry.price) &&
               Objects.equals(odometer, fuelEntry.odometer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, liters, price, odometer);
    }

    @Override
    public String toString() {
        return "FuelEntry{" +
               "id=" + id +
               ", liters=" + liters +
               ", price=" + price +
               ", odometer=" + odometer +
               '}';
    }
}

