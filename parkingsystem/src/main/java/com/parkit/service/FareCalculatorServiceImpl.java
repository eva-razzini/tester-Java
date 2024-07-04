package com.parkit.service;

import com.parkit.model.Ticket;

/**
 * Service class for calculating parking fares.
 */
public class FareCalculatorServiceImpl implements FareCalculatorService {

    private static final double CAR_RATE_PER_HOUR = 1.5;
    private static final double BIKE_RATE_PER_HOUR = 1.0;

    /**
     * Calculates the fare for a given parking ticket.
     *
     * @param ticket the parking ticket
     * @throws IllegalArgumentException if the out time is before the in time or if the parking type is unknown
     */
    @Override
    public void calculateFare(Ticket ticket) {
        validateTicket(ticket);

        long inTime = ticket.getInTime().getTime();
        long outTime = ticket.getOutTime().getTime();

        // Calculate duration in hours
        double duration = calculateDuration(inTime, outTime);

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR:
                ticket.setPrice(duration * CAR_RATE_PER_HOUR);
                break;
            case BIKE:
                ticket.setPrice(duration * BIKE_RATE_PER_HOUR);
                break;
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }

    /**
     * Validates the given parking ticket.
     *
     * @param ticket the parking ticket to validate
     * @throws IllegalArgumentException if the out time is before the in time
     */
    private void validateTicket(Ticket ticket) {
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
            throw new IllegalArgumentException("Out time provided is incorrect: " + ticket.getOutTime());
        }
    }

    /**
     * Calculates the duration in hours between two timestamps.
     *
     * @param inTime  the in time in milliseconds
     * @param outTime the out time in milliseconds
     * @return the duration in hours
     */
    private double calculateDuration(long inTime, long outTime) {
        return (double) (outTime - inTime) / (1000 * 60 * 60);
    }
}


