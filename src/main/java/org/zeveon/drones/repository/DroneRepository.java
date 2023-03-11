package org.zeveon.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zeveon.drones.entity.Drone;

/**
 * @author Stanislav Vafin
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
}
