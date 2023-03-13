package org.zeveon.drones.event;

import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;

import java.util.Map;

/**
 * @author Stanislav Vafin
 */
public class DroneBatteryCheckEvent extends AuditApplicationEvent {

    private static final String DRONE_BATTERY_CHECKED = "DRONE_BATTERY_CHECKED";
    private static final String SYSTEM = "SYSTEM";

    public DroneBatteryCheckEvent(Long droneId, Integer batteryCapacity) {
        super(SYSTEM, DRONE_BATTERY_CHECKED, getDetails(droneId, batteryCapacity));
    }

    private static Map<String, Object> getDetails(Long droneId, Integer batteryCapacity) {
        return Map.of(
                "drone.id", droneId,
                "drone.battery_capacity", batteryCapacity
        );
    }
}
