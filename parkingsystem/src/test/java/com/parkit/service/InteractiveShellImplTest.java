package com.parkit.service;

import com.parkit.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class InteractiveShellImplTest {

    @InjectMocks
    private InteractiveShellImpl interactiveShell;

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private ParkingService parkingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadInterface_ProcessIncomingVehicle() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1).thenReturn(3); // First incoming vehicle, then exit

        interactiveShell.loadInterface();

        verify(parkingService, times(1)).processIncomingVehicle();
        verify(parkingService, never()).processExitingVehicle();
    }

    @Test
    public void loadInterface_ProcessExitingVehicle() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(2).thenReturn(3); // First exiting vehicle, then exit

        interactiveShell.loadInterface();

        verify(parkingService, times(1)).processExitingVehicle();
        verify(parkingService, never()).processIncomingVehicle();
    }

    @Test
    public void loadInterface_InvalidOption() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(4).thenReturn(3); // Invalid option, then exit

        interactiveShell.loadInterface();

        verify(parkingService, never()).processIncomingVehicle();
        verify(parkingService, never()).processExitingVehicle();
    }
}

