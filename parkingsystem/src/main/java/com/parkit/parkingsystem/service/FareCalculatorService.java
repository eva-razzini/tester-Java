package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, boolean discount){
        if ((ticket.getInTime() == null) || (ticket.getOutTime() == null)) {
            throw new IllegalArgumentException("InTime and OutTime cannot be null");
        }

        long inTimeInMillis = ticket.getInTime().getTime();
        long outTimeInMillis = ticket.getOutTime().getTime();

        if (outTimeInMillis < inTimeInMillis) {
            throw new IllegalArgumentException("Out time provided is incorrect: " + ticket.getOutTime().toString());
        }

        long durationInMillis = outTimeInMillis - inTimeInMillis;
        double durationInHours = durationInMillis / (1000.0 * 60 * 60);



        if (durationInHours < 0.5) {
            ticket.setPrice(0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(durationInHours * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationInHours * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
            if (discount) {
                ticket.setPrice(ticket.getPrice() * 0.95);
            }
        }
    }
}