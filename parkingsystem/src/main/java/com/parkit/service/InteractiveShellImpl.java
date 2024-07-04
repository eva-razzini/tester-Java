package com.parkit.service;

import com.parkit.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation class for the interactive shell.
 */
public class InteractiveShellImpl implements InteractiveShell {

    private static final Logger logger = LogManager.getLogger(InteractiveShellImpl.class);

    private final InputReaderUtil inputReaderUtil;
    private final ParkingService parkingService;

    /**
     * Constructs an InteractiveShellImpl with the specified InputReaderUtil and ParkingService.
     *
     * @param inputReaderUtil the input reader utility
     * @param parkingService  the parking service
     */
    public InteractiveShellImpl(InputReaderUtil inputReaderUtil, ParkingService parkingService) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingService = parkingService;
    }

    /**
     * Loads the interactive shell interface.
     */
    @Override
    public void loadInterface() {
        logger.info("App initialized!!!");
        System.out.println("Welcome to Parking System!");

        boolean continueApp = true;

        while (continueApp) {
            loadMenu();
            int option = inputReaderUtil.readSelection();
            switch (option) {
                case 1:
                    handleIncomingVehicle();
                    break;
                case 2:
                    handleExitingVehicle();
                    break;
                case 3:
                    System.out.println("Exiting from the system!");
                    continueApp = false;
                    break;
                default:
                    System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");
            }
        }
    }

    /**
     * Loads the menu options.
     */
    private void loadMenu() {
        System.out.println("Please select an option. Simply enter the number to choose an action");
        System.out.println("1 New Vehicle Entering - Allocate Parking Space");
        System.out.println("2 Vehicle Exiting - Generate Ticket Price");
        System.out.println("3 Shutdown System");
    }

    /**
     * Handles the process for an incoming vehicle.
     */
    private void handleIncomingVehicle() {
        try {
            parkingService.processIncomingVehicle();
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
            System.out.println("Error processing incoming vehicle. Please try again.");
        }
    }

    /**
     * Handles the process for an exiting vehicle.
     */
    private void handleExitingVehicle() {
        try {
            parkingService.processExitingVehicle();
        } catch (Exception e) {
            logger.error("Unable to process exiting vehicle", e);
            System.out.println("Error processing exiting vehicle. Please try again.");
        }
    }
}
