package org.zeveon.drones.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeveon.drones.entity.Medication;
import org.zeveon.drones.repository.MedicationRepository;
import org.zeveon.drones.service.ImageService;
import org.zeveon.drones.service.MedicationService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * @author Stanislav Vafin
 */
@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository repository;

    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    public List<Medication> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medication> getAvailable() {
        return repository.findMedicationsByDroneIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllExistingMedicationImagePaths() {
        return repository.findNotNullMedicationImagePaths();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medication> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medication> getByIds(Collection<Long> ids) {
        return repository.findAllById(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Medication save(Medication medication) {
        return repository.save(medication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Medication> saveAll(Collection<Medication> medications) {
        return repository.saveAll(medications);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Medication delete(Long id) {
        var medication = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no medication with id: %s".formatted(id)));
        repository.delete(medication);
        ofNullable(medication.getImagePath())
                .ifPresent(imageService::remove);
        return medication;
    }
}
