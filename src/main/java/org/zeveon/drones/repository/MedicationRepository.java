package org.zeveon.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zeveon.drones.entity.Medication;

/**
 * @author Stanislav Vafin
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
