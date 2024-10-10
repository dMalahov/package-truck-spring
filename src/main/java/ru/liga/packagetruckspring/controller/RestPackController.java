package ru.liga.packagetruckspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.packagetruckspring.model.Pack;
import ru.liga.packagetruckspring.service.PackService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/pack-truck")
@RequiredArgsConstructor
public class RestPackController {

    private final PackService packService;

    @PostMapping
    public ResponseEntity<String> updatePackageByName(@Valid @RequestBody Pack pack) {
        String result = packService.packTruck(pack.getMode(), pack.getNames(), pack.getTrucksSize(), pack.getFormat());
        return ResponseEntity.ok(result);
    }

}
