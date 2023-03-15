package org.zeveon.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zeveon.drones.entity.Medication;

import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findMedicationsByDroneIsNull();

    @Query("SELECT m.imagePath FROM Medication m WHERE m.imagePath IS NOT NULL")
    List<String> findNotNullMedicationImagePaths();
}
