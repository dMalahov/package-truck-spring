package ru.liga.packagetruckspring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.liga.packagetruckspring.model.Truck;
import ru.liga.packagetruckspring.model.Package;
import ru.liga.packagetruckspring.repository.TruckRepository;
import ru.liga.packagetruckspring.service.TruckService;

@ExtendWith(MockitoExtension.class)
public class TruckServiceTest {

    @Mock
    private TruckRepository truckRepository;

    @InjectMocks
    private TruckService truckService;

    @BeforeEach
    public void setup() {
        List<Truck> truckList = new ArrayList<>();
        truckList.add(new Truck(10, 5));
        lenient().when(truckRepository.findAll()).thenReturn(truckList);
    }

    @Test
    public void testCreateTruck() {
        String params = "10x5,15x10";
        truckService.createTruck(params);
        ArgumentCaptor<Truck> truckCaptor = ArgumentCaptor.forClass(Truck.class);
        verify(truckRepository).clear();
        verify(truckRepository, times(2)).save(truckCaptor.capture());
        List<Truck> capturedTrucks = truckCaptor.getAllValues();
        assertEquals(2, capturedTrucks.size());
        assertEquals(10, capturedTrucks.get(0).getHeight());
        assertEquals(5, capturedTrucks.get(0).getWidth());
        assertEquals(15, capturedTrucks.get(1).getHeight());
        assertEquals(10, capturedTrucks.get(1).getWidth());
    }

    @Test
    public void testCreateSimplePack() {
        List<Package> readyPackages = Arrays.asList(
                new Package(2,2,5,new String[]{"999","999","999"}),
                new Package(5, 2,7,new String[]{"999","999","999"}));
        List<Truck> trucks = truckService.createSimplePack(readyPackages);
        assertEquals(1, trucks.get(0).getPackages().size());
        verify(truckRepository, times(2)).findAll();
    }

    @Test
    public void testCreateComplexPackForWidth() {
        List<Package> readyPackages = Arrays.asList(
                new Package(2,2,5,new String[]{"999","999","999"}),
                new Package(5, 2,7,new String[]{"999","999","999"}));
        List<Truck> trucks = truckService.createComplexPackForWidth(readyPackages);
        assertFalse(trucks.get(0).getPackages().isEmpty());
        verify(truckRepository).findAll();
    }

    @Test
    public void testCreateComplexPackFoHeight() {
        List<Package> readyPackages = Arrays.asList(
                new Package(2,2,5,new String[]{"999","999","999"}),
                new Package(5, 2,7,new String[]{"999","999","999"}));
        List<Truck> trucks = truckService.createComplexPackFoHeight(readyPackages);
        assertEquals(2, trucks.get(0).getPackages().size());
        verify(truckRepository).findAll();
    }
}
