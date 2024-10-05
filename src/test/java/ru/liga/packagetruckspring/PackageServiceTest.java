package ru.liga.packagetruckspring;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.liga.packagetruckspring.model.Package;
import ru.liga.packagetruckspring.repository.PackageRepository;
import ru.liga.packagetruckspring.service.PackageService;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

	@Mock
	private PackageRepository packageRepository;

	@InjectMocks
	private PackageService packageService;

	@Test
	void testSortSimpleOrders() {
		List<String> unassembledPackagesList = Arrays.asList("AAA:BB:CC", "D:E:FFF");
		doNothing().when(packageRepository).clear();
		List<Package> sortedPackages = packageService.sortSimpleOrders(unassembledPackagesList);
		verify(packageRepository, times(1)).clear();
		verify(packageRepository, times(2)).save(any(Package.class));
		assertNotNull(sortedPackages);
	}

}
