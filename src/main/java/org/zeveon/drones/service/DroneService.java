package org.zeveon.drones.service;

import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.entity.Medication;

import java.util.Collection;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
public interface DroneService {

    Drone register(Drone drone);

    Medication loadMedication(Long droneId, Medication medication);

    List<Medication> loadMedications(Long droneId, Collection<Medication> medications);

    List<Medication> loadMedicationsByIds(Long droneId, Collection<Long> medicationIds);

    List<Medication> getMedications(Long droneId);
}
