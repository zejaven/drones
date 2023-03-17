package org.zeveon.drones.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.model.State;
import org.zeveon.drones.validation.annotations.BatteryLevelHigherThan;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Stanislav Vafin
 */
public class BatteryLevelValidatorTest {

    private static final int MIN_BATTERY_CAPACITY = 25;

    private BatteryLevelValidator validator;
    private ConstraintValidatorContext context;

    public static Stream<Arguments> isValidTestParams() {
        return Stream.of(
                arguments(
                        null,
                        true
                ),
                arguments(
                        newDrone(State.LOADING, MIN_BATTERY_CAPACITY - 1),
                        false
                ),
                arguments(
                        newDrone(State.LOADING, MIN_BATTERY_CAPACITY + 1),
                        true
                ),
                arguments(
                        newDrone(State.IDLE, MIN_BATTERY_CAPACITY - 1),
                        true
                ),
                arguments(
                        newDrone(State.IDLE, MIN_BATTERY_CAPACITY + 1),
                        true
                )
        );
    }

    @BeforeEach
    void setUp() {
        var annotation = mock(BatteryLevelHigherThan.class);
        when(annotation.value()).thenReturn(MIN_BATTERY_CAPACITY);

        validator = new BatteryLevelValidator();
        validator.initialize(annotation);
        context = mock(ConstraintValidatorContext.class);
    }

    @ParameterizedTest
    @MethodSource("isValidTestParams")
    void isValidTest(Drone drone, boolean expectedResult) {
        assertEquals(expectedResult, validator.isValid(drone, context));
    }

    private static Drone newDrone(State state, Integer batteryCapacity) {
        return Drone.builder()
                .state(state)
                .batteryCapacity(batteryCapacity)
                .build();
    }
}
