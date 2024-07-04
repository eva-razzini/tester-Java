package com.parkit.service;

import com.parkit.dao.ParkingSpotDAO;
import com.parkit.dao.TicketDAO;
import com.parkit.model.ParkingSpot;
import com.parkit.model.Ticket;
import com.parkit.util.InputReaderUtil;
import com.parkit.constants.ParkingType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Service class for handling parking operations.
 */
public class ParkingServiceImpl implements ParkingService {

    private static final Logger logger = LogManager.getLogger(ParkingServiceImpl.class);

    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;
    private final FareCalculatorService fareCalculatorService;

    /**
     * Constructs a ParkingServiceImpl with the specified dependencies.
     *
     * @param inputReaderUtil       the input reader utility
     * @param parkingSpotDAO        the parking spot DAO
     * @param ticketDAO             the ticket DAO
     * @param fareCalculatorService the fare calculator service
     */
    public ParkingServiceImpl(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO, FareCalculatorService fareCalculatorService) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
        this.fareCalculatorService = fareCalculatorService;
    }

    /**
     * Processes the entry of a new vehicle.
     */
    @Override
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getNumber() > 0) {
                String vehicleRegNumber = getVehicleRegNumber();
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);

                Date inTime = new Date();
                Ticket ticket = new Ticket();
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticketDAO.saveTicket(ticket);

                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number: " + parkingSpot.getNumber());
                System.out.println("Recorded in-time for vehicle number: " + vehicleRegNumber + " is: " + inTime);
            }
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
            System.out.println("Error processing incoming vehicle. Please try again.");
        }
    }

    /**
     * Processes the exit of a vehicle.
     */
    @Override
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);

            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);

                System.out.println("Please pay the parking fare: " + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number: " + vehicleRegNumber + " is: " + outTime);
            } else {
                System.out.println("Unable to update ticket information. Error occurred.");
            }
        } catch (Exception e) {
            logger.error("Unable to process exiting vehicle", e);
            System.out.println("Error processing exiting vehicle. Please try again.");
        }
    }

    /**
     * Gets the vehicle registration number from the user.
     *
     * @return the vehicle registration number
     * @throws Exception if there is an error reading the input
     */
    private String getVehicleRegNumber() throws Exception {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * Gets the next available parking number if available.
     *
     * @return the next available parking spot
     */
    private ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        } catch (Exception e) {
            logger.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * Gets the vehicle type from the user.
     *
     * @return the vehicle type
     */
    private ParkingType getVehicleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1:
                return ParkingType.CAR;
            case 2:
                return ParkingType.BIKE;
            default:
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
        }
    }
}
