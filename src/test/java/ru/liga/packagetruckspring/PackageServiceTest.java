package ru.liga.packagetruckspring;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.packagetruckspring.model.Package;
import ru.liga.packagetruckspring.model.Truck;
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
		List<Package> sortedPackages = packageService.sortSimpleOrders(unassembledPackagesList);
		verify(packageRepository, times(1)).clear();
		verify(packageRepository, times(2)).save(any(Package.class));
		assertNotNull(sortedPackages);
	}

	@Test
	void testSortComplexOrders() {
		List<Truck> truckList = new ArrayList<>();
		Truck truck1 = new Truck(5, 5);
		Truck truck2 = new Truck(5, 5);
		truck1.getPackages().add(
				new Package(3, 3, 3, new String[]{"999", "999", "999"}));
		truck1.getPackages().add(
				new Package(2, 3, 3, new String[]{"666", "666"}));
		truck2.getPackages().add(
				new Package(2, 4, 4, new String[]{"8888", "8888"}));
		truck2.getPackages().add(
				new Package(1, 1, 1, new String[]{"1"}));
		truckList.add(truck1);
		truckList.add(truck2);
		doNothing().when(packageRepository).clear();
		List<Package> sortedPackages = packageService.sortComplexOrders(truckList);
		verify(packageRepository, times(1)).clear();
		verify(packageRepository, times(2)).save(any(Package.class));
		assertNotNull(sortedPackages);
	}

}
