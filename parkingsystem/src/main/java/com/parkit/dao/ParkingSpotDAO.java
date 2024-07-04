package com.parkit.dao;

import com.parkit.config.DataBaseConfig;
import com.parkit.constants.DBConstants;
import com.parkit.constants.ParkingType;
import com.parkit.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for managing parking spots in the database.
 */
public class ParkingSpotDAO implements IParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger(ParkingSpotDAO.class);
    private final DataBaseConfig dataBaseConfig;

    /**
     * Constructs a ParkingSpotDAO with the specified DataBaseConfig.
     *
     * @param dataBaseConfig the database configuration
     */
    public ParkingSpotDAO(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    /**
     * Gets the next available parking spot for the specified parking type.
     *
     * @param parkingType the type of parking spot (CAR/BIKE)
     * @return the parking spot number, or -1 if none available
     */
    @Override
    public int getNextAvailableSlot(ParkingType parkingType) {
        int result = -1;
        logger.info("Fetching next available slot for parking type: {}", parkingType);
        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)) {

            ps.setString(1, parkingType.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            logger.error("Error fetching next available slot for parking type: {}", parkingType, ex);
        }
        return result;
    }

    /**
     * Updates the availability of the specified parking spot.
     *
     * @param parkingSpot the parking spot to update
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean updateParking(ParkingSpot parkingSpot) {
        logger.info("Updating parking spot: {}", parkingSpot.getNumber());
        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)) {

            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getNumber());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (SQLException ex) {
            logger.error("Error updating parking spot: {}", parkingSpot.getNumber(), ex);
            return false;
        }
    }

    /**
     * Gets the parking spot with the specified ID.
     *
     * @param parkingSpotId the ID of the parking spot
     * @return the parking spot, or null if not found
     */
    @Override
    public ParkingSpot getParkingSpot(int parkingSpotId) {
        logger.info("Fetching parking spot with ID: {}", parkingSpotId);
        ParkingSpot parkingSpot = null;
        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.GET_PARKING_SPOT)) {

            ps.setInt(1, parkingSpotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(2)), rs.getBoolean(3));
                }
            }
        } catch (SQLException ex) {
            logger.error("Error fetching parking spot with ID: {}", parkingSpotId, ex);
        }
        return parkingSpot;
    }
}
