package org.zeveon.drones.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.entity.Medication;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

/**
 * @author Stanislav Vafin
 */
public class DroneItemWeightValidatorTest {

    private DroneItemWeightValidator validator;
    private ConstraintValidatorContext context;

    public static Stream<Arguments> isValidTestParams() {
        return Stream.of(
                arguments(
                        null,
                        true
                ),
                arguments(
                        newDrone(List.of(
                                newMedication(25),
                                newMedication(30),
                                newMedication(40)
                        )),
                        true
                ),
                arguments(
                        newDrone(List.of(
                                newMedication(25),
                                newMedication(30),
                                newMedication(45)
                        )),
                        true
                ),
                arguments(
                        newDrone(List.of(
                                newMedication(25),
                                newMedication(30),
                                newMedication(50)
                        )),
                        false
                )
        );
    }

    @BeforeEach
    void setUp() {
        validator = new DroneItemWeightValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @ParameterizedTest
    @MethodSource("isValidTestParams")
    void isValidTest(Drone drone, boolean expectedResult) {
        assertEquals(expectedResult, validator.isValid(drone, context));
    }

    private static Drone newDrone(List<Medication> medications) {
        return Drone.builder()
                .weightLimit(100)
                .medications(medications)
                .build();
    }

    private static Medication newMedication(Integer weight) {
        return Medication.builder()
                .weight(weight)
                .build();
    }
}
