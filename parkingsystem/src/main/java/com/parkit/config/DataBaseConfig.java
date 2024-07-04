package com.parkit.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConfig {
    private static final Logger logger = LogManager.getLogger(DataBaseConfig.class);
    private Properties properties;

    public DataBaseConfig() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Sorry, unable to find application.properties");
                throw new IOException("application.properties not found in the classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            logger.error("Error loading database configuration", ex);
            throw new RuntimeException("Failed to load database configuration", ex);
        }
    }

    public Connection getConnection() throws SQLException {
        logger.info("Creating DB connection");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        if (url == null || username == null || password == null) {
            logger.error("Database connection properties not set");
            throw new SQLException("Database connection properties not set");
        }

        return DriverManager.getConnection(url, username, password);
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }
}

