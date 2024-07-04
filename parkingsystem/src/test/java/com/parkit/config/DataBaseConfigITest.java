package com.parkit.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DataBaseConfigITest {

    private DataBaseConfig dataBaseConfig;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        dataBaseConfig = new DataBaseConfig();
    }

    @AfterEach
    public void tearDown() {
        dataBaseConfig.closeConnection(connection);
    }

    @Test
    @DisplayName("Test getConnection - Success")
    public void testGetConnectionSuccess() {
        assertDoesNotThrow(() -> {
            connection = dataBaseConfig.getConnection();
            assertNotNull(connection, "Connection should not be null");
        });
    }

    @Test
    @DisplayName("Test getConnection - Invalid Credentials")
    public void testGetConnectionInvalidCredentials() {
        DataBaseConfig invalidConfig = new DataBaseConfig() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/prod",
                        "invalid_user",
                        "invalid_password");
            }
        };

        assertThrows(SQLException.class, invalidConfig::getConnection, "Expected SQLException to be thrown");
    }
}
