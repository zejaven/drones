package org.zeveon.drones.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeveon.drones.dto.DroneDto;
import org.zeveon.drones.mapper.DroneMapper;
import org.zeveon.drones.service.DroneService;

/**
 * @author Stanislav Vafin
 */
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
}
