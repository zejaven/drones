package org.zeveon.drones.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.entity.Medication;
import org.zeveon.drones.model.State;
import org.zeveon.drones.repository.DroneRepository;
import org.zeveon.drones.service.DroneService;
import org.zeveon.drones.service.MedicationService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
    @Transactional(readOnly = true)
    public List<Drone> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drone> getAvailable() {
        return repository.findAllByStateEquals(State.IDLE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Medication loadMedication(Long droneId, Medication medication) {
        var drone = repository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(droneId)));
        drone.setState(State.LOADING);
        medication.setDrone(drone);
        drone.setMedications(Stream.concat(
                drone.getMedications().stream(),
                Stream.of(medication)
        ).toList());
        return medicationService.save(medication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Medication> loadMedications(Long droneId, Collection<Medication> medications) {
        var drone = repository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(droneId)));
        drone.setState(State.LOADING);
        medications.forEach(medication -> medication.setDrone(drone));
        drone.setMedications(Stream.concat(
                drone.getMedications().stream(),
                medications.stream()
        ).toList());
        return medicationService.saveAll(medications);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Medication> loadMedicationsByIds(Long droneId, Collection<Long> medicationIds) {
        var drone = repository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(droneId)));
        drone.setState(State.LOADING);
        var medications = medicationService.getByIds(medicationIds);
        medications.forEach(medication -> medication.setDrone(drone));
        drone.setMedications(Stream.concat(
                drone.getMedications().stream(),
                medications.stream()
        ).toList());
        return medications;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medication> getMedications(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(id)))
                .getMedications();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getBatteryLevel(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(id)))
                .getBatteryCapacity();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Drone changeState(Long id, State state) {
        var drone = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(id)));
        drone.setState(state);
        return drone;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Drone delete(Long id) {
        var drone = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no drone with id: %s".formatted(id)));
        repository.delete(drone);
        return drone;
    }
}
