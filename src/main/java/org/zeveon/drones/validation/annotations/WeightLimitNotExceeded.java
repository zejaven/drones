package org.zeveon.drones.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.zeveon.drones.validation.DroneItemWeightValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Stanislav Vafin
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DroneItemWeightValidator.class)
public @interface WeightLimitNotExceeded {
    String message() default "Total weight of all items exceeds weight limit";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

