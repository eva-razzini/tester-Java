package com.parkit.service;

import com.parkit.dao.ParkingSpotDAO;
import com.parkit.dao.TicketDAO;
import com.parkit.model.ParkingSpot;
import com.parkit.model.Ticket;
import com.parkit.util.InputReaderUtil;
import com.parkit.constants.ParkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParkingServiceImplTest {

    @InjectMocks
    private ParkingServiceImpl parkingService;

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private TicketDAO ticketDAO;

    @Mock
    private FareCalculatorService fareCalculatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processIncomingVehicle_Success() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABC123");
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processIncomingVehicle();

        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processExitingVehicle_Success() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABC123");
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setInTime(new Date(System.currentTimeMillis() - 3600000)); // 1 hour parking
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        parkingService.processExitingVehicle();

        verify(fareCalculatorService, times(1)).calculateFare(any(Ticket.class));
        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processIncomingVehicle_Error() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(new Exception("Test exception"));
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, never()).saveTicket(any(Ticket.class));
    }

    @Test
    public void processExitingVehicle_Error() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(new Exception("Test exception"));
        parkingService.processExitingVehicle();
        verify(fareCalculatorService, never()).calculateFare(any(Ticket.class));
        verify(ticketDAO, never()).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
    }
}
