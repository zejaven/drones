package org.zeveon.drones.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.validation.annotations.BatteryLevelHigherThan;

/**
 * @author Stanislav Vafin
 */
public class BatteryLevelValidator implements ConstraintValidator<BatteryLevelHigherThan, Drone> {

    private int minValue;

    @Override
    public void initialize(BatteryLevelHigherThan annotation) {
        minValue = annotation.value();
    }

    @Override
    public boolean isValid(Drone drone, ConstraintValidatorContext context) {
        return drone == null
                || drone.getBatteryCapacity() >= minValue;
    }
}

