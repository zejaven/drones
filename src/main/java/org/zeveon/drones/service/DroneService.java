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

    List<Medication> loadMedications(Long droneId, Collection<Medication> medications);
}
