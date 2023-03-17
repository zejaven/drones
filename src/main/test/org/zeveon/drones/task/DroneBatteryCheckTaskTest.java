package org.zeveon.drones.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.zeveon.drones.entity.Drone;
import org.zeveon.drones.event.DroneBatteryCheckEvent;
import org.zeveon.drones.service.DroneService;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Stanislav Vafin
 */
@ExtendWith(MockitoExtension.class)
public class DroneBatteryCheckTaskTest {

    @Mock
    private DroneService droneService;

    @Mock
    private ApplicationEventPublisher publisher;

    @Captor
    private ArgumentCaptor<DroneBatteryCheckEvent> eventCaptor;

    @InjectMocks
    private DroneBatteryCheckTask droneBatteryCheckTask;

    @Test
    void testCheckBatteryLevels() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var drones = Arrays.asList(newDrone(1L, 75), newDrone(2L, 90));

        when(droneService.getAll()).thenReturn(drones);

        droneBatteryCheckTask.checkBatteryLevels();

        verify(droneService, times(1)).getAll();
        verify(publisher, times(2)).publishEvent(eventCaptor.capture());

        var publishedEvents = eventCaptor.getAllValues();

        var getDetailsMethod = DroneBatteryCheckEvent.class.getDeclaredMethod("getDetails", Long.class, Integer.class);
        getDetailsMethod.setAccessible(true);

        for (int i = 0; i < publishedEvents.size(); i++) {
            var expectedDrone = drones.get(i);
            var actualEvent = publishedEvents.get(i);

            var actualDetails = (Map<?, ?>) getDetailsMethod.invoke(actualEvent, expectedDrone.getId(), expectedDrone.getBatteryCapacity());

            assertEquals(expectedDrone.getId(), actualDetails.get("drone.id"));
            assertEquals(expectedDrone.getBatteryCapacity(), actualDetails.get("drone.battery_capacity"));
        }
    }

    private static Drone newDrone(Long id, Integer batteryCapacity) {
        return Drone.builder()
                .id(id)
                .batteryCapacity(batteryCapacity)
                .build();
    }
}
