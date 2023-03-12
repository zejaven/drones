package org.zeveon.drones.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.entity.Medication;
import org.zeveon.drones.repository.DroneRepository;
import org.zeveon.drones.service.DroneService;
import org.zeveon.drones.service.MedicationService;

import java.util.Collection;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository repository;

    private final MedicationService medicationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Drone register(Drone drone) {
        return repository.save(drone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Medication loadMedication(Long droneId, Medication medication) {
        var drone = repository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(droneId)));
        medication.setDrone(drone);
        return medicationService.save(medication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Medication> loadMedications(Long droneId, Collection<Medication> medications) {
        var drone = repository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(droneId)));
        medications.forEach(medication -> medication.setDrone(drone));
        return medicationService.saveAll(medications);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Medication> loadMedicationsByIds(Long droneId, Collection<Long> medicationIds) {
        var drone = repository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(droneId)));
        var medications = medicationService.getByIds(medicationIds);
        medications.forEach(medication -> medication.setDrone(drone));
        return medications;
    }
}
