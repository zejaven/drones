package org.zeveon.drones.service;

import org.zeveon.drones.entity.Medication;

import java.util.Collection;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
public interface MedicationService {

    Medication save(Medication medication);

    List<Medication> saveAll(Collection<Medication> medications);
}
