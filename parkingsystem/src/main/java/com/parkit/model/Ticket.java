package com.parkit.model;

import java.util.Date;

/**
 * Represents a parking ticket in the parking system.
 */
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    /**
     * Default constructor.
     */
    public Ticket() {
        // Default constructor
    }

    /**
     * Constructs a Ticket with the specified details.
     *
     * @param id the unique ID of the ticket
     * @param parkingSpot the parking spot associated with the ticket
     * @param vehicleRegNumber the vehicle registration number
     * @param price the price of the parking
     * @param inTime the in-time of the vehicle
     * @param outTime the out-time of the vehicle
     */
    public Ticket(int id, ParkingSpot parkingSpot, String vehicleRegNumber, double price, Date inTime, Date outTime) {
        this.id = id;
        this.parkingSpot = parkingSpot;
        this.vehicleRegNumber = vehicleRegNumber;
        this.price = price;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    /**
     * Gets the unique ID of the ticket.
     *
     * @return the ID of the ticket
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the ticket.
     *
     * @param id the new ID of the ticket
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the parking spot associated with the ticket.
     *
     * @return the parking spot
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Sets the parking spot associated with the ticket.
     *
     * @param parkingSpot the new parking spot
     */
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * Gets the vehicle registration number.
     *
     * @return the vehicle registration number
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Sets the vehicle registration number.
     *
     * @param vehicleRegNumber the new vehicle registration number
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Gets the price of the parking.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the parking.
     *
     * @param price the new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the in-time of the vehicle.
     *
     * @return the in-time
     */
    public Date getInTime() {
        return inTime;
    }

    /**
     * Sets the in-time of the vehicle.
     *
     * @param inTime the new in-time
     */
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    /**
     * Gets the out-time of the vehicle.
     *
     * @return the out-time
     */
    public Date getOutTime() {
        return outTime;
    }

    /**
     * Sets the out-time of the vehicle.
     *
     * @param outTime the new out-time
     */
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", parkingSpot=" + parkingSpot +
                ", vehicleRegNumber='" + vehicleRegNumber + '\'' +
                ", price=" + price +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                '}';
    }
}
