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
import org.zeveon.drones.service.ImageService;

import java.io.IOException;
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

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DroneDto> register(@RequestBody @Valid DroneDto droneDto) {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDto(
                        service.register(
                                DroneMapper.INSTANCE.toEntity(droneDto))));
    }

    @GetMapping("/available")
    public ResponseEntity<List<DroneDto>> getAvailable() {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDtoList(
                        service.getAvailable()));
    }

    @PostMapping(value = "/{drone_id}/load-medication", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicationDto> loadMedication(
            @PathVariable("drone_id") Long id,
            @ModelAttribute @Valid MedicationDto medicationDto
    ) {
        try {
            return ResponseEntity.ok(
                    MedicationMapper.INSTANCE.toDto(
                            service.loadMedication(id,
                                    MedicationMapper.INSTANCE.toEntity(medicationDto, imageService))));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/{drone_id}/load-medications", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicationDto>> loadMedications(
            @PathVariable("drone_id") Long id,
            @RequestBody @Valid List<MedicationDto> medicationDtos
    ) {
        try {
            return ResponseEntity.ok(
                    MedicationMapper.INSTANCE.toDtoList(
                            service.loadMedications(id,
                                    MedicationMapper.INSTANCE.toEntityList(medicationDtos, imageService))));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/{drone_id}/load-medications-by-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicationDto>> loadMedicationsByIds(
            @PathVariable("drone_id") Long id,
            @RequestBody List<Long> medicationDtoIds
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.loadMedicationsByIds(id, medicationDtoIds)));
    }

    @GetMapping("/{drone_id}/medications")
    public ResponseEntity<List<MedicationDto>> getMedications(@PathVariable("drone_id") Long id) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.getMedications(id)));
    }
}
