package org.zeveon.drones.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.zeveon.drones.model.State;
import org.zeveon.drones.service.DroneService;
import org.zeveon.drones.service.ImageService;

import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drones")
public class DroneController {

    private final DroneService service;

    private final ImageService imageService;

    @Operation(summary = "Register drone", description = "Register a new drone in the system")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DroneDto> register(@RequestBody @Valid DroneDto droneDto) {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDto(
                        service.register(
                                DroneMapper.INSTANCE.toEntity(droneDto))));
    }

    @Operation(summary = "Get all drones", description = "Get all drones registered in the system")
    @GetMapping
    public ResponseEntity<List<DroneDto>> getAll() {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDtoList(
                        service.getAll()));
    }

    @Operation(summary = "Get available drones", description = "Get drones in IDLE state which are ready for loading")
    @GetMapping("/available")
    public ResponseEntity<List<DroneDto>> getAvailable() {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDtoList(
                        service.getAvailable()));
    }

    @Operation(summary = "Load medication", description = "Load drone with medication item")
    @PostMapping(value = "/{drone_id}/load-medication", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicationDto> loadMedication(
            @Parameter(description = "ID of the drone to load")
            @PathVariable("drone_id") Long id,
            @ModelAttribute @Valid MedicationDto medicationDto
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDto(
                        service.loadMedication(id,
                                MedicationMapper.INSTANCE.toEntity(medicationDto, imageService))));
    }

    @Operation(summary = "Load medications", description = "Load drone with several medication items")
    @PostMapping(value = "/{drone_id}/load-medications", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicationDto>> loadMedications(
            @Parameter(description = "ID of the drone to load")
            @PathVariable("drone_id") Long id,
            @RequestBody @Valid List<MedicationDto> medicationDtos
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.loadMedications(id,
                                MedicationMapper.INSTANCE.toEntityList(medicationDtos, imageService))));
    }

    @Operation(summary = "Load existing medications", description = "Load drone with several existing medication items")
    @PostMapping(value = "/{drone_id}/load-medications-by-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicationDto>> loadMedicationsByIds(
            @Parameter(description = "ID of the drone to load")
            @PathVariable("drone_id") Long id,
            @Parameter(description = "ID of medication items for loading onto the drone")
            @RequestBody List<Long> medicationDtoIds
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.loadMedicationsByIds(id, medicationDtoIds)));
    }

    @Operation(summary = "Get drone medications", description = "Get medications loaded onto the drone")
    @GetMapping("/{drone_id}/medications")
    public ResponseEntity<List<MedicationDto>> getMedications(
            @Parameter(description = "ID of the drone to get medications")
            @PathVariable("drone_id") Long id
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.getMedications(id)));
    }

    @Operation(summary = "Check drone battery level", description = "Check drone battery level")
    @GetMapping("/{drone_id}/battery")
    public ResponseEntity<Integer> checkBatteryLevel(
            @Parameter(description = "ID of the drone to check battery level")
            @PathVariable("drone_id") Long id
    ) {
        return ResponseEntity.ok(service.getBatteryLevel(id));
    }

    @Operation(summary = "Change drone state", description = "Change drone state")
    @PostMapping("/{drone_id}/change-state/{state}")
    public ResponseEntity<DroneDto> changeState(
            @Parameter(description = "ID of the drone to change state")
            @PathVariable("drone_id") Long id,
            @Parameter(description = "The state to change the drone to")
            @PathVariable("state") State state
    ) {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDto(
                        service.changeState(id, state)));
    }

    @Operation(summary = "Delete drone", description = "Delete drone from the system")
    @DeleteMapping("/{drone_id}")
    public ResponseEntity<DroneDto> delete(
            @Parameter(description = "ID of the drone to delete")
            @PathVariable("drone_id") Long id
    ) {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDto(
                        service.delete(id)));
    }
}
