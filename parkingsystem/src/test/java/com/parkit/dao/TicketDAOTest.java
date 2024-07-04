package com.parkit.dao;

import com.parkit.config.DataBaseConfig;
import com.parkit.constants.ParkingType;
import com.parkit.model.ParkingSpot;
import com.parkit.model.Ticket;
import com.parkit.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketDAOTest {

    @InjectMocks
    private TicketDAO ticketDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private Ticket ticket;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setVehicleRegNumber("ABC123");
        ticket.setPrice(10.0);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        when(dataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void saveTicket_Success() throws Exception {
        doNothing().when(mockConnection).setAutoCommit(false);
        doNothing().when(mockConnection).commit();
        doNothing().when(mockConnection).rollback();

        assertDoesNotThrow(() -> ticketDAO.saveTicket(ticket));

        verify(mockPreparedStatement, times(1)).setInt(1, ticket.getParkingSpot().getNumber());
        verify(mockPreparedStatement, times(1)).setString(2, ticket.getVehicleRegNumber());
        verify(mockPreparedStatement, times(1)).setDouble(3, ticket.getPrice());
        verify(mockPreparedStatement, times(1)).setTimestamp(4, DateUtil.toTimestamp(ticket.getInTime()));
        verify(mockPreparedStatement, times(1)).setTimestamp(5, DateUtil.toTimestamp(ticket.getOutTime()));
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockConnection, times(1)).commit();
    }


    @Test
    public void getTicket_Success() throws Exception {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
        when(mockResultSet.getInt(2)).thenReturn(1);
        when(mockResultSet.getString(3)).thenReturn("ABC123");
        when(mockResultSet.getDouble(4)).thenReturn(10.0);
        when(mockResultSet.getTimestamp(5)).thenReturn(new Timestamp(ticket.getInTime().getTime()));
        when(mockResultSet.getTimestamp(6)).thenReturn(new Timestamp(ticket.getOutTime().getTime()));
        when(mockResultSet.getString(6)).thenReturn("CAR");

        Ticket fetchedTicket = ticketDAO.getTicket("ABC123");
        assertNotNull(fetchedTicket);
        assertEquals(1, fetchedTicket.getId());
        assertEquals("ABC123", fetchedTicket.getVehicleRegNumber());
        assertEquals(10.0, fetchedTicket.getPrice());
        assertNotNull(fetchedTicket.getInTime());
        assertNotNull(fetchedTicket.getOutTime());
    }

    @Test
    public void updateTicket_Success() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = ticketDAO.updateTicket(ticket);
        assertTrue(result);

        verify(mockPreparedStatement, times(1)).setDouble(1, ticket.getPrice());
        verify(mockPreparedStatement, times(1)).setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
        verify(mockPreparedStatement, times(1)).setInt(3, ticket.getId());
    }

    @Test
    public void getNbTicket_Success() throws Exception {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(5);

        int count = ticketDAO.getNbTicket("ABC123");
        assertEquals(5, count);

        verify(mockPreparedStatement, times(1)).setString(1, "ABC123");
    }
}


