package org.zeveon.drones.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Get all medications", description = "Get all medications added in the system")
    @GetMapping
    public ResponseEntity<List<MedicationDto>> getMedications() {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.getAll()));
    }

    @Operation(summary = "Get available medications", description = "Get medications which were not loaded onto the drone")
    @GetMapping("/available")
    public ResponseEntity<List<MedicationDto>> getAvailable() {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.getAvailable()));
    }

    @Operation(summary = "Get medication", description = "Get medication")
    @GetMapping("/{medication_id}")
    public ResponseEntity<MedicationDto> getMedication(
            @Parameter(description = "ID of the medication item")
            @PathVariable("medication_id") Long id
    ) {
        return service.getById(id)
                .map(m -> ResponseEntity.ok(MedicationMapper.INSTANCE.toDto(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get medication image", description = "Get medication image",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Medication image",
                    content = {
                            @Content(mediaType = "image/png"),
                            @Content(mediaType = "image/jpeg")
                    }
            ))
    @GetMapping("/{medication_id}/image")
    public ResponseEntity<Resource> getMedicationImage(
            @Parameter(description = "ID of the medication item to get image")
            @PathVariable("medication_id") Long id
    ) {
        return service.getById(id)
                .map(m -> Pair.of(imageService.get(m.getImagePath()), m.getImageContentType()))
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf(p.getSecond()))
                        .body(p.getFirst()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add medication", description = "Add medication in the system")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicationDto> addMedication(@ModelAttribute @Valid MedicationDto medicationDto) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDto(
                        service.save(
                                MedicationMapper.INSTANCE.toEntity(medicationDto, imageService))));
    }

    @Operation(summary = "Add medications", description = "Add several medication items")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicationDto>> addMedications(@RequestBody @Valid List<MedicationDto> medicationDtos) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.saveAll(
                                MedicationMapper.INSTANCE.toEntityList(medicationDtos, imageService))));
    }

    @Operation(summary = "Unload medication", description = "Unload medication from drone")
    @PostMapping("/unload/{medication_id}")
    public ResponseEntity<MedicationDto> unloadMedication(
            @Parameter(description = "ID of the medication item to unload")
            @PathVariable("medication_id") Long id
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDto(
                        service.unload(id)));
    }

    @Operation(summary = "Unload medications", description = "Unload several medication items from drone")
    @PostMapping(value = "/unload", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicationDto>> unloadMedications(
            @Parameter(description = "IDs of medication items to unload")
            @RequestBody List<Long> ids
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDtoList(
                        service.unload(ids)));
    }

    @Operation(summary = "Delete medication", description = "Delete medication item")
    @DeleteMapping("/{medication_id}")
    public ResponseEntity<MedicationDto> delete(
            @Parameter(description = "ID of medication item to delete")
            @PathVariable("medication_id") Long id
    ) {
        return ResponseEntity.ok(
                MedicationMapper.INSTANCE.toDto(
                        service.delete(id)));
    }
}
