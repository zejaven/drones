package org.zeveon.drones.dto;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zeveon.drones.model.Model;
import org.zeveon.drones.model.State;

/**
 * @author Stanislav Vafin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DroneDto {

    @Null
    private Long id;

    private String serialNumber;

    private Model model;

    private Integer weightLimit;

    private Integer batteryCapacity;

    private State state;
}
