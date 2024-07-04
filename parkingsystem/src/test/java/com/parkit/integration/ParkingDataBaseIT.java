package com.parkit.integration;

import com.parkit.config.DataBaseConfig;
import com.parkit.service.DataBasePrepareService;
import com.parkit.dao.ParkingSpotDAO;
import com.parkit.dao.TicketDAO;
import com.parkit.model.ParkingSpot;
import com.parkit.model.Ticket;
import com.parkit.service.FareCalculatorServiceImpl;
import com.parkit.service.ParkingService;
import com.parkit.service.ParkingServiceImpl;
import com.parkit.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseConfig dataBaseTestConfig = new DataBaseConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO(dataBaseTestConfig);
        ticketDAO = new TicketDAO(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService(dataBaseTestConfig);
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown() {
        // Any cleanup code if needed
    }

    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, new FareCalculatorServiceImpl());
        parkingService.processIncomingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticket, "Ticket should be saved in the database");
        assertNotNull(ticket.getInTime(), "In-time should be populated");

        ParkingSpot parkingSpot = parkingSpotDAO.getParkingSpot(ticket.getParkingSpot().getNumber());
        assertNotNull(parkingSpot, "Parking spot should exist");
        assertFalse(parkingSpot.isAvailable(), "Parking spot should be marked as unavailable");
    }

    @Test
    public void testParkingLotExit() {
        testParkingACar();
        ParkingService parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, new FareCalculatorServiceImpl());
        parkingService.processExitingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticket.getOutTime(), "Out-time should be populated");
        assertTrue(ticket.getPrice() > 0, "Price should be calculated and set");

        ParkingSpot parkingSpot = parkingSpotDAO.getParkingSpot(ticket.getParkingSpot().getNumber());
        assertTrue(parkingSpot.isAvailable(), "Parking spot should be marked as available");
    }
}
