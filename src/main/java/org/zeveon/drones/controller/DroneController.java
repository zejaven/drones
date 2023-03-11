package org.zeveon.drones.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zeveon.drones.dto.DroneDto;
import org.zeveon.drones.dto.MedicationDto;
import org.zeveon.drones.mapper.DroneMapper;
import org.zeveon.drones.mapper.MedicationMapper;
import org.zeveon.drones.service.DroneService;

import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drone")
public class DroneController {

    private final DroneService droneService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DroneDto> register(@RequestBody @Valid DroneDto droneDto) {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDto(
                        droneService.register(
                                DroneMapper.INSTANCE.toEntity(droneDto))));
    }

    @PostMapping(value = "/{id}/load-medications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicationDto>> loadMedications(
            @PathVariable("id") Long id,
            @RequestBody @Valid List<MedicationDto> medicationDtos
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        droneService.loadMedications(id,
                                MedicationMapper.INSTANCE.toEntityList(medicationDtos))));
    }
}
