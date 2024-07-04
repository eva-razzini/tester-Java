package com.parkit.dao;

import com.parkit.config.DataBaseConfig;
import com.parkit.constants.DBConstants;
import com.parkit.constants.ParkingType;
import com.parkit.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParkingSpotDAOTest {

    @InjectMocks
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement ps;

    @Mock
    private ResultSet rs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetNextAvailableSlot() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        int slot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(1, slot);

        verify(con, times(1)).prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
        verify(ps, times(1)).setString(1, ParkingType.CAR.toString());
        verify(rs, times(1)).next();
    }

    @Test
    public void testUpdateParking() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);

        boolean result = parkingSpotDAO.updateParking(parkingSpot);
        assertTrue(result);

        verify(ps, times(1)).setBoolean(1, parkingSpot.isAvailable());
        verify(ps, times(1)).setInt(2, parkingSpot.getNumber());
    }

    @Test
    public void testGetParkingSpot() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.GET_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("CAR");
        when(rs.getBoolean(3)).thenReturn(true);

        ParkingSpot parkingSpot = parkingSpotDAO.getParkingSpot(1);
        assertNotNull(parkingSpot);
        assertEquals(1, parkingSpot.getNumber());
        assertEquals(ParkingType.CAR, parkingSpot.getParkingType());
        assertTrue(parkingSpot.isAvailable());
    }
}
