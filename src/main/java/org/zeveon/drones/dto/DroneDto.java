package org.zeveon.drones.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
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

    @Size(max = 100)
    private String serialNumber;

    private Model model;

    @Max(500)
    private Integer weightLimit;

    @NotNull
    @Max(100)
    private Integer batteryCapacity;

    private State state;
}
