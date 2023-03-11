package org.zeveon.drones.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.repository.DroneRepository;
import org.zeveon.drones.service.DroneService;

/**
 * @author Stanislav Vafin
 */
@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Drone register(Drone drone) {
        return droneRepository.save(drone);
    }
}
