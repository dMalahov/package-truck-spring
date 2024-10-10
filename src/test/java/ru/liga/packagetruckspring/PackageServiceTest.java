package ru.liga.packagetruckspring;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.packagetruckspring.dto.PackageDto;
import ru.liga.packagetruckspring.dto.TruckDto;
import ru.liga.packagetruckspring.repository.PackageRepository;
import ru.liga.packagetruckspring.service.PackageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

	@Mock
	private PackageRepository packageRepository;

	@InjectMocks
	private PackageService packageService;

	@Test
	void testSortSimpleOrders() {
		List<String> unassembledPackagesList = Arrays.asList("AAA:AA:AA", "D:D:DDD");
		doNothing().when(packageRepository).clear();
		List<PackageDto> sortedPackages = packageService.sortSimpleOrders(unassembledPackagesList);
		verify(packageRepository, times(1)).clear();
		verify(packageRepository, times(2)).save(any(PackageDto.class));
		assertNotNull(sortedPackages);
	}

	@Test
	void testSortComplexOrders() {
		List<TruckDto> truckList = new ArrayList<>();
		TruckDto truck1 = new TruckDto();
		TruckDto truck2 = new TruckDto();
		truck1.setHeight(5);
		truck1.setWidth(5);
		truck1.setPackages(new ArrayList<>());
		truck2.setHeight(5);
		truck2.setWidth(5);
		truck2.setPackages(new ArrayList<>());
		truck1.getPackages().add(
				new PackageDto(1L, 3, 3, 3, new String[]{"999", "999", "999"}));
		truck1.getPackages().add(
				new PackageDto(2L, 2, 3, 3, new String[]{"666", "666"}));
		truck2.getPackages().add(
				new PackageDto(3L, 2, 4, 4, new String[]{"8888", "8888"}));
		truck2.getPackages().add(
				new PackageDto(4L, 1, 1, 1, new String[]{"1"}));
		truckList.add(truck1);
		truckList.add(truck2);
		doNothing().when(packageRepository).clear();
		List<PackageDto> sortedPackages = packageService.sortComplexOrders(truckList);
		verify(packageRepository, times(1)).clear();
		verify(packageRepository, times(2)).save(any(PackageDto.class));
		assertNotNull(sortedPackages);
	}

}
