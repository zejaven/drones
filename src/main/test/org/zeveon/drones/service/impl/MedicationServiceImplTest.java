package org.zeveon.drones.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.entity.Medication;
import org.zeveon.drones.model.State;
import org.zeveon.drones.repository.MedicationRepository;
import org.zeveon.drones.service.ImageService;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Stanislav Vafin
 */
@ExtendWith(MockitoExtension.class)
public class MedicationServiceImplTest {

    @Mock
    private MedicationRepository repository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private MedicationServiceImpl medicationService;

    public static Stream<Arguments> deleteTestParams() {
        return Stream.of(
                arguments(
                        "images\\be550f4f-0e27-4cae-8acc-4801814b03b9_image.jpg",
                        1
                ),
                arguments(
                        null,
                        0
                )
        );
    }

    @Test
    public void getAllTest() {
        var medication = newMedication(null, null);

        when(repository.findAll()).thenReturn(singletonList(medication));

        var result = medicationService.getAll();

        verify(repository).findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(medication));
    }

    @Test
    public void getAvailableTest() {
        var medication = newMedication(null, null);

        when(repository.findMedicationsByDroneIsNull()).thenReturn(singletonList(medication));

        var result = medicationService.getAvailable();

        verify(repository).findMedicationsByDroneIsNull();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(medication));
    }

    @Test
    public void getAllExistingMedicationImagePathsTest() {
        var imagePath = "images\\be550f4f-0e27-4cae-8acc-4801814b03b9_image.jpg";

        when(repository.findNotNullMedicationImagePaths()).thenReturn(singletonList(imagePath));

        var result = medicationService.getAllExistingMedicationImagePaths();

        verify(repository).findNotNullMedicationImagePaths();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(imagePath));
    }

    @Test
    public void getByIdTest() {
        var medication = newMedication(null, null);

        when(repository.findById(anyLong())).thenReturn(Optional.of(medication));

        var result = medicationService.getById(medication.getId());

        verify(repository).findById(medication.getId());

        assertNotNull(result);
        assertTrue(result.filter(medication::equals).isPresent());
    }

    @Test
    public void getByIdsTest() {
        var medication = newMedication(null, null);

        when(repository.findAllById(anyList())).thenReturn(singletonList(medication));

        var result = medicationService.getByIds(singletonList(medication.getId()));

        verify(repository).findAllById(singletonList(medication.getId()));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(medication));
    }

    @Test
    public void saveTest() {
        var medication = newMedication(null, null);

        when(repository.save(any(Medication.class))).thenReturn(medication);

        var result = medicationService.save(medication);

        verify(repository).save(medication);

        assertEquals(medication, result);
    }

    @Test
    public void saveAllTest() {
        var medication = newMedication(null, null);

        when(repository.saveAll(anyList())).thenReturn(singletonList(medication));

        var result = medicationService.saveAll(singletonList(medication));

        verify(repository).saveAll(singletonList(medication));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(medication));
    }

    @Test
    public void unloadTest() {
        var medication = newMedication(newDrone(), null);

        when(repository.findById(anyLong())).thenReturn(Optional.of(medication));

        var result = medicationService.unload(medication.getId());

        verify(repository).findById(medication.getId());

        assertNotNull(result);
        assertEquals(medication.getId(), result.getId());
        assertNull(result.getDrone());
    }

    @Test
    public void unloadNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> medicationService.unload(1L));

        assertEquals("There is no medication with id: 1", exception.getMessage());
    }

    @Test
    public void unloadTest2() {
        var medication = newMedication(newDrone(), null);

        when(repository.findAllById(anyList())).thenReturn(singletonList(medication));

        var result = medicationService.unload(singletonList(medication.getId()));

        verify(repository).findAllById(singletonList(medication.getId()));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(medication));
        assertNull(result.iterator().next().getDrone());
    }

    @ParameterizedTest
    @MethodSource("deleteTestParams")
    public void deleteTest(String imagePath, int invocations) {
        var medication = newMedication(null, imagePath);

        when(repository.findById(anyLong())).thenReturn(Optional.of(medication));
        doNothing().when(repository).delete(any(Medication.class));
        lenient().doNothing().when(imageService).remove(anyString());

        var result = medicationService.delete(medication.getId());

        verify(repository).findById(medication.getId());
        verify(repository).delete(medication);
        verify(imageService, times(invocations)).remove(imagePath);

        assertNotNull(result);
        assertEquals(medication.getId(), result.getId());
    }

    @Test
    public void deleteNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> medicationService.delete(1L));

        assertEquals("There is no medication with id: 1", exception.getMessage());
    }

    private static Drone newDrone() {
        return Drone.builder()
                .id(1L)
                .batteryCapacity(100)
                .state(State.IDLE)
                .build();
    }

    private static Medication newMedication(Drone drone, String imagePath) {
        return Medication.builder()
                .id(1L)
                .drone(drone)
                .imagePath(imagePath)
                .build();
    }
}
