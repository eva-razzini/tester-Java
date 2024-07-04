package com.parkit.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataBaseConfigTest {

    @InjectMocks
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection mockConnection;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Test getConnection - Success")
    public void testGetConnectionSuccess() {
        try {
            // Mock the DriverManager to return a mock connection
            mockStatic(DriverManager.class);
            when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Connection connection = dataBaseConfig.getConnection();
            assertNotNull(connection, "Connection should not be null");
            DriverManager.getConnection(anyString(), anyString(), anyString());

        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    @DisplayName("Test getConnection - SQLException")
    public void testGetConnectionSQLException() {
        try {
            // Mock the DriverManager to throw a SQLException
            mock(DriverManager.class);
            when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenThrow(new SQLException());

            assertThrows(SQLException.class, () -> dataBaseConfig.getConnection(), "Expected SQLException to be thrown");

        } catch (SQLException e) {
            fail("ClassNotFoundException should not have been thrown");
        }
    }

    @Test
    @DisplayName("Test closeConnection - Success")
    public void testCloseConnectionSuccess() {
        try {
            dataBaseConfig.closeConnection(mockConnection);
            verify(mockConnection, times(1)).close();
        } catch (SQLException e) {
            fail("SQLException should not have been thrown");
        }
    }

    @Test
    @DisplayName("Test closeConnection - Null Connection")
    public void testCloseConnectionNull() {
        assertDoesNotThrow(() -> dataBaseConfig.closeConnection(null), "Exception should not be thrown for null connection");
    }

    @Test
    @DisplayName("Test closeConnection - SQLException")
    public void testCloseConnectionSQLException() {
        try {
            doThrow(new SQLException()).when(mockConnection).close();
            assertDoesNotThrow(() -> dataBaseConfig.closeConnection(mockConnection), "Exception should not be thrown");
            verify(mockConnection, times(1)).close();
        } catch (SQLException e) {
            fail("SQLException should not have been thrown");
        }
    }
}

