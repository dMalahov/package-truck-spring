package ru.liga.packagetruckspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.packagetruckspring.dto.StructureDto;
import ru.liga.packagetruckspring.service.StructureService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class RestPackageController {

    private final StructureService structureService;

    @GetMapping("/list")
    public List<StructureDto> getPackages() {
        return structureService.getAllStructures();
    }

    @PutMapping("/update")
    public List<StructureDto> updatePackages(@Valid @RequestBody List<StructureDto> structures) {
        return structureService.reloadStructures(structures);
    }

    @GetMapping("/package/{name}")
    public ResponseEntity<StructureDto> getPackageByName(@PathVariable String name) {
        StructureDto structure = structureService.getStructureByName(name);
        return ResponseEntity.ok(structure);
    }

    @PostMapping("/package/update/{name}")
    public ResponseEntity<StructureDto> updatePackageByName(@PathVariable String name, @Valid @RequestBody StructureDto structure) {
        StructureDto newStructure = structureService.updateStructure(name, structure);
        return ResponseEntity.ok(newStructure);
    }

}
