package ru.liga.packagetruckspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.packagetruckspring.dto.StructureDto;
import ru.liga.packagetruckspring.service.PackService;
import ru.liga.packagetruckspring.service.*;

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
	private final FileService fileService;
	private final PackService packService;

	@ShellMethod("list-packages")
	public void listPackages() {
		System.out.println(structureService.getAllStructuresString());
	}

	@ShellMethod("reload-packages")
	public void reloadPackages(@ShellOption String filePath) {
		List<StructureDto> structure = fileService.readFileData(filePath);
		structureService.reloadStructures(structure);
	}

	@ShellMethod("print-package")
	public void printPackage(@ShellOption String name) {
		System.out.println(structureService.getStructureByNameString(name));
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
	public void packTruck(@ShellOption String mode, @ShellOption("--listName") String listName, @ShellOption("--size") String size, @ShellOption String result) {
		String finalTruck = packService.packTruck(mode, listName, size, result);
		if(result.toUpperCase(Locale.ROOT).contains("JSON")) {
			fileService.writeResultJson(finalTruck);
		} else {
			System.out.println(finalTruck);
			;
		}
	}


}
