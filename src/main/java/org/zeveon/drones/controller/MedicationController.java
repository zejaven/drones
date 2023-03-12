package org.zeveon.drones.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zeveon.drones.dto.MedicationDto;
import org.zeveon.drones.mapper.MedicationMapper;
import org.zeveon.drones.service.ImageService;
import org.zeveon.drones.service.MedicationService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService service;

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<MedicationDto>> getMedications() {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.getAll()));
    }

    @GetMapping("/available")
    public ResponseEntity<List<MedicationDto>> getAvailable() {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.getAvailable()));
    }

    @GetMapping("/{medication_id}")
    public ResponseEntity<MedicationDto> getMedication(@PathVariable("medication_id") Long id) {
        return service.getById(id)
                .map(m -> ResponseEntity.ok(MedicationMapper.INSTANCE.toDto(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{medication_id}/image")
    public ResponseEntity<Resource> getMedicationImage(@PathVariable("medication_id") Long id) {
        return service.getById(id)
                .map(m -> {
                    try {
                        return Pair.of(imageService.get(m.getImagePath()), m.getImageContentType());
                    } catch (MalformedURLException e) {
                        return null;
                    }
                })
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf(p.getSecond()))
                        .body(p.getFirst()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicationDto> addMedication(@ModelAttribute @Valid MedicationDto medicationDto) {
        try {
            return ResponseEntity.ok(
                    MedicationMapper.INSTANCE.toDto(
                            service.save(
                                    MedicationMapper.INSTANCE.toEntity(medicationDto, imageService))));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicationDto>> addMedications(@RequestBody @Valid List<MedicationDto> medicationDtos) {
        try {
            return ResponseEntity.ok(
                    MedicationMapper.INSTANCE.toDtoList(
                            service.saveAll(
                                    MedicationMapper.INSTANCE.toEntityList(medicationDtos, imageService))));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
