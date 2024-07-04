package com.parkit.dao;

import com.parkit.model.Ticket;

public interface ITicketDAO {
    void saveTicket(Ticket ticket);
    Ticket getTicket(String vehicleRegNumber);
    boolean updateTicket(Ticket ticket);
    int getNbTicket(String vehicleRegNumber);
}
