package org.zeveon.drones.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeveon.drones.entity.Medication;
import org.zeveon.drones.repository.MedicationRepository;
import org.zeveon.drones.service.MedicationService;

import java.util.Collection;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository repository;

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
}
