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
import org.zeveon.drones.repository.DroneRepository;
import org.zeveon.drones.service.MedicationService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
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
public class DroneServiceImplTest {

    @Mock
    private DroneRepository repository;

    @Mock
    private MedicationService medicationService;

    @InjectMocks
    private DroneServiceImpl droneService;

    private static Stream<Arguments> loadMedicationTestParams() {
        return Stream.of(
                arguments(
                        newDrone(emptyList()),
                        emptyList(),
                        1
                ),
                arguments(
                        newDrone(singletonList(newMedication(2L))),
                        singletonList(newMedication(2L)),
                        2
                )
        );
    }

    public static Stream<Arguments> loadMedicationsTestParams() {
        return Stream.of(
                arguments(
                        newDrone(emptyList()),
                        emptyList(),
                        2
                ),
                arguments(
                        newDrone(singletonList(newMedication(3L))),
                        singletonList(newMedication(3L)),
                        3
                )
        );
    }

    @Test
    public void registerTest() {
        var drone = newDrone(emptyList());

        when(repository.save(any(Drone.class))).thenReturn(drone);

        var result = droneService.register(drone);

        verify(repository).save(drone);

        assertNotNull(result);
        assertEquals(drone.getId(), result.getId());
    }

    @Test
    public void getAllTest() {
        var drone = newDrone(emptyList());

        when(repository.findAll()).thenReturn(singletonList(drone));

        var result = droneService.getAll();

        verify(repository).findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(drone));
    }

    @Test
    public void getAvailableTest() {
        var drone = newDrone(emptyList());

        when(repository.findAllByStateEquals(State.IDLE)).thenReturn(singletonList(drone));

        var result = droneService.getAvailable();

        verify(repository).findAllByStateEquals(State.IDLE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(drone));
    }

    @ParameterizedTest
    @MethodSource("loadMedicationTestParams")
    public void loadMedicationTest(Drone drone, List<Medication> existingMedications, int expectedSize) {
        var medication = newMedication(1L);

        when(repository.findById(anyLong())).thenReturn(Optional.of(drone));
        when(medicationService.save(any(Medication.class))).thenReturn(medication);

        var result = droneService.loadMedication(drone.getId(), medication);

        verify(repository).findById(drone.getId());
        verify(medicationService).save(medication);

        assertNotNull(result);
        assertEquals(medication.getId(), result.getId());
        assertEquals(State.LOADING, drone.getState());
        assertEquals(drone, result.getDrone());
        assertNotNull(drone.getMedications());
        assertEquals(expectedSize, drone.getMedications().size());
        assertTrue(drone.getMedications().contains(medication));
        assertTrue(drone.getMedications().containsAll(existingMedications));
    }

    @Test
    public void loadMedicationNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> droneService.loadMedication(1L, newMedication(1L)));

        verifyNoInteractions(medicationService);
        assertEquals("There is no drone with id: 1", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("loadMedicationsTestParams")
    public void loadMedicationsTest(Drone drone, List<Medication> existingMedications, int expectedSize) {
        var medications = List.of(newMedication(1L), newMedication(2L));

        when(repository.findById(anyLong())).thenReturn(Optional.of(drone));
        when(medicationService.saveAll(anyList())).thenReturn(medications);

        var result = droneService.loadMedications(drone.getId(), medications);

        verify(repository).findById(drone.getId());
        verify(medicationService).saveAll(medications);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(medications.containsAll(result));
        assertEquals(State.LOADING, drone.getState());
        assertEquals(drone, result.get(0).getDrone());
        assertEquals(drone, result.get(1).getDrone());
        assertNotNull(drone.getMedications());
        assertEquals(expectedSize, drone.getMedications().size());
        assertTrue(drone.getMedications().containsAll(medications));
        assertTrue(drone.getMedications().containsAll(existingMedications));
    }

    @Test
    public void loadMedicationsNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> droneService.loadMedications(1L, singletonList(newMedication(1L))));

        verifyNoInteractions(medicationService);
        assertEquals("There is no drone with id: 1", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("loadMedicationsTestParams")
    public void loadMedicationsByIdsTest(Drone drone, List<Medication> existingMedications, int expectedSize) {
        var medicationIds = List.of(1L, 2L);
        var medications = List.of(newMedication(1L), newMedication(2L));

        when(repository.findById(anyLong())).thenReturn(Optional.of(drone));
        when(medicationService.getByIds(anyList())).thenReturn(medications);

        var result = droneService.loadMedicationsByIds(drone.getId(), medicationIds);

        verify(repository).findById(drone.getId());
        verify(medicationService).getByIds(medicationIds);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(medications.containsAll(result));
        assertEquals(State.LOADING, drone.getState());
        assertEquals(drone, result.get(0).getDrone());
        assertEquals(drone, result.get(1).getDrone());
        assertNotNull(drone.getMedications());
        assertEquals(expectedSize, drone.getMedications().size());
        assertTrue(drone.getMedications().containsAll(medications));
        assertTrue(drone.getMedications().containsAll(existingMedications));
    }

    @Test
    public void loadMedicationsByIdsNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> droneService.loadMedicationsByIds(1L, singletonList(1L)));

        verifyNoInteractions(medicationService);
        assertEquals("There is no drone with id: 1", exception.getMessage());
    }

    @Test
    public void getMedicationsTest() {
        var medication = newMedication(1L);
        var drone = newDrone(singletonList(medication));

        when(repository.findById(anyLong())).thenReturn(Optional.of(drone));

        var result = droneService.getMedications(drone.getId());

        verify(repository).findById(drone.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(medication));
    }

    @Test
    public void getMedicationsNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> droneService.getMedications(1L));

        assertEquals("There is no drone with id: 1", exception.getMessage());
    }

    @Test
    public void getBatteryLevelTest() {
        var drone = newDrone(emptyList());

        when(repository.findById(anyLong())).thenReturn(Optional.of(drone));

        var result = droneService.getBatteryLevel(drone.getId());

        verify(repository).findById(drone.getId());

        assertEquals(100, result);
    }

    @Test
    public void getBatteryLevelNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> droneService.getBatteryLevel(1L));

        assertEquals("There is no drone with id: 1", exception.getMessage());
    }

    @Test
    public void changeStateTest() {
        var drone = newDrone(emptyList());

        when(repository.findById(anyLong())).thenReturn(Optional.of(drone));

        var result = droneService.changeState(drone.getId(), State.LOADED);

        verify(repository).findById(drone.getId());

        assertNotNull(result);
        assertEquals(drone.getId(), result.getId());
        assertEquals(State.LOADED, result.getState());
    }

    @Test
    public void changeStateNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> droneService.changeState(1L, State.LOADED));

        assertEquals("There is no drone with id: 1", exception.getMessage());
    }

    @Test
    public void deleteTest() {
        var drone = newDrone(emptyList());

        when(repository.findById(anyLong())).thenReturn(Optional.of(drone));
        doNothing().when(repository).delete(any(Drone.class));

        var result = droneService.delete(drone.getId());

        verify(repository).findById(drone.getId());
        verify(repository).delete(drone);

        assertNotNull(result);
        assertEquals(drone.getId(), result.getId());
    }

    @Test
    public void deleteNegativeTest() {
        when(repository.findById(anyLong())).thenReturn(empty());

        var exception = assertThrows(RuntimeException.class,
                () -> droneService.delete(1L));

        assertEquals("There is no drone with id: 1", exception.getMessage());
    }

    private static Drone newDrone(List<Medication> medications) {
        return Drone.builder()
                .id(1L)
                .batteryCapacity(100)
                .state(State.IDLE)
                .medications(medications)
                .build();
    }

    private static Medication newMedication(Long id) {
        return Medication.builder()
                .id(id)
                .build();
    }
}
