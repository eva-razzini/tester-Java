package com.parkit.service;

import com.parkit.config.DataBaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Service class for preparing the database for integration tests.
 */
public class DataBasePrepareService {

    private final DataBaseConfig dataBaseConfig;

    public DataBasePrepareService(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public void clearDataBaseEntries() {
        Connection connection = null;
        try {
            connection = dataBaseConfig.getConnection();
            // PreparedStatements allow us to avoid SQL injection attacks
            connection.prepareStatement("UPDATE parking SET available = true").execute();
            connection.prepareStatement("TRUNCATE TABLE ticket").execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseConfig.closeConnection(connection);
        }
    }
}
