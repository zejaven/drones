package org.zeveon.drones.service;

import org.zeveon.drones.entity.Medication;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Stanislav Vafin
 */
public interface MedicationService {

    List<Medication> getAll();

    List<Medication> getAvailable();

    List<String> getAllExistingMedicationImagePaths();

    Optional<Medication> getById(Long id);

    List<Medication> getByIds(Collection<Long> ids);

    Medication save(Medication medication);

    List<Medication> saveAll(Collection<Medication> medications);
}
