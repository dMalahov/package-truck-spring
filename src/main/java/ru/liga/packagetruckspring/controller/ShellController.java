package ru.liga.packagetruckspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.packagetruckspring.model.Package;
import ru.liga.packagetruckspring.model.Structure;
import ru.liga.packagetruckspring.model.Truck;
import ru.liga.packagetruckspring.service.FileService;
import ru.liga.packagetruckspring.service.PackageService;
import ru.liga.packagetruckspring.service.StructureService;
import ru.liga.packagetruckspring.service.TruckService;

import java.util.List;
import java.util.Locale;

/**
 * ShellController отвечает за обработку команд оболочки, связанных с операциями с пакетами и грузовиками.
 * Он взаимодействует с различными службами для выполнения операций, таких как перечисление, перезагрузка, добавление,
 * обновление и упаковка пакетов.
 */
@ShellComponent
@RequiredArgsConstructor
public class ShellController {

	private final StructureService structureService;
	private final PackageService packageService;
	private final TruckService truckService;
	private final FileService fileService;

	@ShellMethod("list-packages")
	public void listPackages() {
		structureService.printList();
	}

	@ShellMethod("reload-packages")
	public void reloadPackages(@ShellOption String fileName) {
		List<Structure> structure = fileService.readFileData(fileName);
		structureService.reloadStructures(structure);
	}

	@ShellMethod("print-package")
	public void printPackage(@ShellOption String name) {
		structureService.printByName(name);
	}

	@ShellMethod("add-package")
	public void addPackage(@ShellOption String name, @ShellOption("--form") String form) {
		structureService.addNewStructure(name,form);
	}

	@ShellMethod("update-package")
	public void updatePackage(@ShellOption String oldName, @ShellOption String name, @ShellOption("--form") String form) {
		structureService.updateStructure(oldName,name,form);
	}

	@ShellMethod("update-form")
	public void updateForm(@ShellOption String name, @ShellOption("--form") String form) {
		structureService.updateForm(name,form);
	}

	@ShellMethod("update-symbol")
	public void updateSymbol(@ShellOption String name, @ShellOption String symbol) {
		structureService.updateSymbol(name, symbol);
	}

	@ShellMethod("update-name")
	public void updateName(@ShellOption String oldName, @ShellOption String name) {
		structureService.updateName(oldName,name);
	}

	@ShellMethod("pack-truck")
	public void pack(@ShellOption String mode, @ShellOption("--listName") String listName, @ShellOption("--size") String size, @ShellOption String result) {
		List<Truck> trucks;
		List<String> packages;
		truckService.createTruck(size);
		if(listName.isEmpty()) {
			packages = structureService.getAllForms();
		} else {
			packages = structureService.getFormsForName(listName);
		}
		List<Package> sortPackages = packageService.sortSimpleOrders(packages);
		if(mode.toUpperCase(Locale.ROOT).contains("S")) {
			trucks = truckService.createSimplePack(sortPackages);
		} else {
			List<Truck> trucksForWidth = truckService.createComplexPackForWidth(sortPackages);
			List<Package> sortPackagesComplex = packageService.sortComplexOrders(trucksForWidth);
			truckService.createTruck(size);
			trucks = truckService.createComplexPackFoHeight(sortPackagesComplex);
		}
		if(result.toUpperCase(Locale.ROOT).contains("JSON")) {
			fileService.writeResultJson(truckService.createJsonForTruck(trucks));
		} else {
			truckService.printTruckInConsole(trucks);
		}
	}


}
