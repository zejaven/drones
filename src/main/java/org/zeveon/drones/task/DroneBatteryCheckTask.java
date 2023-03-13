package org.zeveon.drones.task;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeveon.drones.event.DroneBatteryCheckEvent;
import org.zeveon.drones.service.DroneService;

/**
 * @author Stanislav Vafin
 */
@Component
@RequiredArgsConstructor
public class DroneBatteryCheckTask {

    private final DroneService droneService;

    private final ApplicationEventPublisher publisher;

    @Scheduled(fixedDelay = 60000)
    public void checkBatteryLevels() {
        droneService.getAll().forEach(drone ->
                publisher.publishEvent(new DroneBatteryCheckEvent(drone.getId(), drone.getBatteryCapacity())));
    }
}
