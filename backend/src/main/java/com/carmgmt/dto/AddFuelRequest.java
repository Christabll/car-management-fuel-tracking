package com.carmgmt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO for adding a fuel entry
 */
public class AddFuelRequest {
    @NotNull(message = "Liters cannot be null")
    @Positive(message = "Liters must be a positive number")
    private Double liters;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    @NotNull(message = "Odometer cannot be null")
    @Min(value = 0, message = "Odometer must be a non-negative number")
    private Integer odometer;

    public Double getLiters() {
        return liters;
    }

    public void setLiters(Double liters) {
        this.liters = liters;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getOdometer() {
        return odometer;
    }

    public void setOdometer(Integer odometer) {
        this.odometer = odometer;
    }
}

