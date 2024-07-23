package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @InjectMocks
    private ParkingService parkingService;

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private TicketDAO ticketDAO;

    @Mock
    private FareCalculatorService fareCalculatorService;

    @BeforeEach
    public void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processIncomingCarSuccessTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService.processIncomingVehicle();

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
        verify(ticketDAO, times(1)).getNbTicket("ABCDEF");
    }

    @Test
    public void processExitingVehicleTest() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        parkingService.processExitingVehicle();
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
        verify(fareCalculatorService, never()).calculateFare(any(Ticket.class), anyBoolean());
        verify(ticketDAO, never()).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
    }

}
