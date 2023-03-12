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
@RequestMapping("/api/drone")
public class DroneController {

    private final DroneService droneService;

    private final ImageService imageService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DroneDto> register(@RequestBody @Valid DroneDto droneDto) {
        return ResponseEntity.ok(
                DroneMapper.INSTANCE.toDto(
                        droneService.register(
                                DroneMapper.INSTANCE.toEntity(droneDto))));
    }

    @PostMapping(value = "/{drone_id}/load-medication", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicationDto> loadMedication(
            @PathVariable("drone_id") Long id,
            @ModelAttribute @Valid MedicationDto medicationDto
    ) {
        try {
            return ResponseEntity.ok(
                    MedicationMapper.INSTANCE.toDto(
                            droneService.loadMedication(id,
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
                            droneService.loadMedications(id,
                                    MedicationMapper.INSTANCE.toEntityList(medicationDtos, imageService))));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
