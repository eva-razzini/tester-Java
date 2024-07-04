package com.parkit;

import com.parkit.config.DataBaseConfig;
import com.parkit.dao.ParkingSpotDAO;
import com.parkit.dao.TicketDAO;
import com.parkit.service.FareCalculatorServiceImpl;
import com.parkit.service.InteractiveShellImpl;
import com.parkit.service.ParkingServiceImpl;
import com.parkit.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Initializing Parking System");

        // Initialize dependencies
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO(dataBaseConfig);
        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        FareCalculatorServiceImpl fareCalculatorService = new FareCalculatorServiceImpl();

        // Create service instances
        ParkingServiceImpl parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);

        // Start interactive shell
        InteractiveShellImpl interactiveShell = new InteractiveShellImpl(inputReaderUtil, parkingService);
        interactiveShell.loadInterface();
    }
}

