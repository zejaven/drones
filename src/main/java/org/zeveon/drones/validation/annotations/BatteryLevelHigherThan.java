package org.zeveon.drones.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.zeveon.drones.validation.BatteryLevelValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Stanislav Vafin
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BatteryLevelValidator.class)
public @interface BatteryLevelHigherThan {
    String message() default "Battery level must be higher than {value}%";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value();
}

