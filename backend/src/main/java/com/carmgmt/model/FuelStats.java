package com.carmgmt.model;

import java.util.Objects;

/**
 * Fuel statistics DTO
 */
public class FuelStats {
    private Double totalFuel;
    private Double totalCost;
    private Double averageConsumption;

    public FuelStats() {
    }

    public FuelStats(Double totalFuel, Double totalCost, Double averageConsumption) {
        this.totalFuel = totalFuel;
        this.totalCost = totalCost;
        this.averageConsumption = averageConsumption;
    }

    public Double getTotalFuel() {
        return totalFuel;
    }

    public void setTotalFuel(Double totalFuel) {
        this.totalFuel = totalFuel;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(Double averageConsumption) {
        this.averageConsumption = averageConsumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelStats fuelStats = (FuelStats) o;
        return Objects.equals(totalFuel, fuelStats.totalFuel) &&
               Objects.equals(totalCost, fuelStats.totalCost) &&
               Objects.equals(averageConsumption, fuelStats.averageConsumption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalFuel, totalCost, averageConsumption);
    }

    @Override
    public String toString() {
        return "FuelStats{" +
               "totalFuel=" + totalFuel +
               ", totalCost=" + totalCost +
               ", averageConsumption=" + averageConsumption +
               '}';
    }
}

