package com.parkit.model;

import com.parkit.constants.ParkingType;

import java.util.Objects;

/**
 * Represents a parking spot in the parking system.
 */
public class ParkingSpot {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    /**
     * Constructs a ParkingSpot with the specified number, type, and availability.
     *
     * @param number      the unique number of the parking spot
     * @param parkingType the type of the parking spot (CAR/BIKE)
     * @param isAvailable the availability status of the parking spot
     */
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * Gets the unique number of the parking spot.
     *
     * @return the number of the parking spot
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the unique number of the parking spot.
     *
     * @param number the new number of the parking spot
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Gets the type of the parking spot.
     *
     * @return the parking type (CAR/BIKE)
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * Sets the type of the parking spot.
     *
     * @param parkingType the new parking type (CAR/BIKE)
     */
    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * Checks if the parking spot is available.
     *
     * @return true if the parking spot is available, false otherwise
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the parking spot.
     *
     * @param available the new availability status
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "number=" + number +
                ", parkingType=" + parkingType +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

