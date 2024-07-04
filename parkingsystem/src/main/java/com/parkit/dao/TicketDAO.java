package com.parkit.dao;

import com.parkit.config.DataBaseConfig;
import com.parkit.constants.DBConstants;
import com.parkit.constants.ParkingType;
import com.parkit.model.ParkingSpot;
import com.parkit.model.Ticket;
import com.parkit.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * DAO class for managing tickets in the database.
 */
public class TicketDAO implements ITicketDAO {
    private static final Logger logger = LogManager.getLogger(TicketDAO.class);
    private final DataBaseConfig dataBaseConfig;

    /**
     * Constructs a TicketDAO with the specified DataBaseConfig.
     *
     * @param dataBaseConfig the database configuration
     */
    public TicketDAO(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    /**
     * Saves a ticket in the database.
     *
     * @param ticket the ticket to save
     */
    @Override
    public void saveTicket(Ticket ticket) {
        logger.info("Saving ticket for vehicle number: {}", ticket.getVehicleRegNumber());
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            con.setAutoCommit(false); // Begin transaction
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            ps.setInt(1, ticket.getParkingSpot().getNumber());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, DateUtil.toTimestamp(ticket.getInTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : DateUtil.toTimestamp(ticket.getOutTime()));
            ps.executeUpdate();
            con.commit(); // Commit transaction
        } catch (SQLException ex) {
            logger.error("Error saving ticket for vehicle number: {}", ticket.getVehicleRegNumber(), ex);
            if (con != null) {
                try {
                    con.rollback(); // Rollback transaction in case of error
                } catch (SQLException rollbackEx) {
                    logger.error("Error rolling back transaction", rollbackEx);
                }
            }
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
    }

    /**
     * Retrieves a ticket by vehicle registration number.
     *
     * @param vehicleRegNumber the vehicle registration number
     * @return the ticket, or null if not found
     */
    @Override
    public Ticket getTicket(String vehicleRegNumber) {
        logger.info("Fetching ticket for vehicle number: {}", vehicleRegNumber);
        Ticket ticket = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_TICKET);
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
                ticket = new Ticket();
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
            }
        } catch (SQLException ex) {
            logger.error("Error fetching ticket for vehicle number: {}", vehicleRegNumber, ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return ticket;
    }

    /**
     * Updates a ticket in the database.
     *
     * @param ticket the ticket to update
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean updateTicket(Ticket ticket) {
        logger.info("Updating ticket for vehicle number: {}", ticket.getVehicleRegNumber());
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            con.setAutoCommit(false); // Begin transaction
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3, ticket.getId());
            int rowCount = ps.executeUpdate();
            con.commit(); // Commit transaction
            return (rowCount == 1);
        } catch (SQLException ex) {
            logger.error("Error updating ticket for vehicle number: {}", ticket.getVehicleRegNumber(), ex);
            if (con != null) {
                try {
                    con.rollback(); // Rollback transaction in case of error
                } catch (SQLException rollbackEx) {
                    logger.error("Error rolling back transaction", rollbackEx);
                }
            }
            return false;
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
    }

    /**
     * Gets the number of tickets for a vehicle.
     *
     * @param vehicleRegNumber the vehicle registration number
     * @return the number of tickets
     */
    @Override
    public int getNbTicket(String vehicleRegNumber) {
        logger.info("Fetching ticket count for vehicle number: {}", vehicleRegNumber);
        int count = 0;
        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.GET_NB_TICKET)) {

            ps.setString(1, vehicleRegNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            logger.error("Error fetching ticket count for vehicle number: {}", vehicleRegNumber, ex);
        }
        return count;
    }
}
