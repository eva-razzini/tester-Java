package com.parkit.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DataBaseConfig {
    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
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
        logger.info("Create DB connection");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC Driver not found", e);
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password"));
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

    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }
}
