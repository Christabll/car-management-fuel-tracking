package com.carmgmt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
}

