package com.parkit.dao;

import com.parkit.constants.ParkingType;
import com.parkit.model.ParkingSpot;

public interface IParkingSpotDAO {
    int getNextAvailableSlot(ParkingType parkingType);
    boolean updateParking(ParkingSpot parkingSpot);
    ParkingSpot getParkingSpot(int parkingSpotId);
}
