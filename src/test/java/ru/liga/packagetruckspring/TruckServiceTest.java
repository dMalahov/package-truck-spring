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
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.packagetruckspring.dto.PackageDto;
import ru.liga.packagetruckspring.dto.TruckDto;
import ru.liga.packagetruckspring.model.Truck;
import ru.liga.packagetruckspring.repository.TruckRepository;
import ru.liga.packagetruckspring.service.TruckService;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@ExtendWith(MockitoExtension.class)
class TruckServiceTest {

    @Mock
    private TruckRepository truckRepository;

    @InjectMocks
    private TruckService truckService;

    @BeforeEach
    public void setup() {
        List<TruckDto> truckList = new ArrayList<>();
        TruckDto truck1 = new TruckDto();
        truck1.setHeight(5);
        truck1.setWidth(5);
        truck1.setPackages(new ArrayList<>());
        PackageDto package1 = new PackageDto(1L, 3, 3, 3, new String[]{"999", "999", "999"});
        truck1.getPackages().add(package1);
        lenient().when(truckRepository.findAll()).thenReturn(truckList);
    }

    @Test
    public void testCreateTruck() {
        String params = "10x5,15x10";
        truckService.createTruck(params);
        ArgumentCaptor<TruckDto> truckCaptor = ArgumentCaptor.forClass(TruckDto.class);
        verify(truckRepository).clear();
        verify(truckRepository, times(2)).save(truckCaptor.capture());
        List<TruckDto> capturedTrucks = truckCaptor.getAllValues();
        assertEquals(2, capturedTrucks.size());
        assertEquals(10, capturedTrucks.get(0).getHeight());
        assertEquals(5, capturedTrucks.get(0).getWidth());
        assertEquals(15, capturedTrucks.get(1).getHeight());
        assertEquals(10, capturedTrucks.get(1).getWidth());
    }

    @Test
    public void testCreateSimplePack() {
        List<PackageDto> readyPackages = Arrays.asList(
                new PackageDto(1L, 2, 2, 5, new String[]{"999", "999", "999"}),
                new PackageDto(2L, 5, 2, 7, new String[]{"999", "999", "999"}));
        List<TruckDto> trucks = truckService.createSimplePack(readyPackages);
        assertEquals(1, trucks.get(0).getPackages().size());
        verify(truckRepository, times(2)).findAll();
    }

    @Test
    public void testCreateComplexPackForWidth() {
        List<PackageDto> readyPackages = Arrays.asList(
                new PackageDto(1L, 2, 2, 5, new String[]{"999", "999", "999"}),
                new PackageDto(2L, 5, 2, 7, new String[]{"999", "999", "999"}));
        List<TruckDto> trucks = truckService.createComplexPackForWidth(readyPackages);
        assertFalse(trucks.get(0).getPackages().isEmpty());
        verify(truckRepository).findAll();
    }

    @Test
    public void testCreateComplexPackFoHeight() {
        List<PackageDto> readyPackages = Arrays.asList(
                new PackageDto(1L, 2, 2, 5, new String[]{"999", "999", "999"}),
                new PackageDto(2L, 5, 2, 7, new String[]{"999", "999", "999"}));
        List<TruckDto> trucks = truckService.createComplexPackFoHeight(readyPackages);
        assertEquals(2, trucks.get(0).getPackages().size());
        verify(truckRepository).findAll();
    }
}
