package org.zeveon.drones.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.entity.Medication;
import org.zeveon.drones.validation.annotations.WeightLimitNotExceeded;

/**
 * @author Stanislav Vafin
 */
public class DroneItemWeightValidator implements ConstraintValidator<WeightLimitNotExceeded, Drone> {

    @Override
    public boolean isValid(Drone drone, ConstraintValidatorContext context) {
        return drone == null
                || drone.getMedications().stream()
                .mapToInt(Medication::getWeight).sum() <= drone.getWeightLimit();
    }
}

