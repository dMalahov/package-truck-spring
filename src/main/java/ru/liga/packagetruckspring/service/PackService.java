package ru.liga.packagetruckspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.dto.PackageDto;
import ru.liga.packagetruckspring.dto.TruckDto;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PackService {

    private final StructureService structureService;
    private final PackageService packageService;
    private final TruckService truckService;

    public String packTruck(String mode, String listName, String size, String result) {
        String finalTruckFormat;
        List<TruckDto> trucks = pack(mode, listName, size);
        if (result.toUpperCase(Locale.ROOT).contains("JSON")) {
            finalTruckFormat = truckService.createJsonForTruck(trucks);
        } else {
            finalTruckFormat = truckService.printTrucks(trucks);
        }
        return finalTruckFormat;
    }

    private List<TruckDto> pack(String mode, String listName, String size) {
        truckService.createTruck(size);
        List<String> packages = getPackages(listName);
        List<PackageDto> sortPackages = packageService.sortSimpleOrders(packages);
        return getTrucks(mode, size, sortPackages);
    }

    private List<String> getPackages(String listName) {
        List<String> packages;
        if (listName.isEmpty()) {
            packages = structureService.getAllForms();
        } else {
            packages = structureService.getFormsForName(listName);
        }
        return packages;
    }

    private List<TruckDto> getTrucks(String mode, String size, List<PackageDto> sortPackages) {
        List<TruckDto> trucks;
        if (mode.toUpperCase(Locale.ROOT).contains("S")) {
            trucks = truckService.createSimplePack(sortPackages);
        } else {
            List<TruckDto> trucksForWidth = truckService.createComplexPackForWidth(sortPackages);
            List<PackageDto> sortPackagesComplex = packageService.sortComplexOrders(trucksForWidth);
            trucks = truckService.createComplexPackFoHeight(sortPackagesComplex);
        }
        return trucks;
    }

}
