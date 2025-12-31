package com.carmgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Request DTO for creating a car
 */
public class CreateCarRequest {
    @NotBlank(message = "Brand cannot be blank")
    private String brand;
    
    @NotBlank(message = "Model cannot be blank")
    private String model;
    
    @NotNull(message = "Year cannot be null")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateCarRequest that = (CreateCarRequest) o;
        return Objects.equals(brand, that.brand) &&
               Objects.equals(model, that.model) &&
               Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model, year);
    }

    @Override
    public String toString() {
        return "CreateCarRequest{" +
               "brand='" + brand + '\'' +
               ", model='" + model + '\'' +
               ", year=" + year +
               '}';
    }
}

