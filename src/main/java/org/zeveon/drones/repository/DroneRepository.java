package org.zeveon.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.model.State;

import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    List<Drone> findAllByStateEquals(State state);
}
