package org.zeveon.drones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Drone information data")
public class DroneDto {

    @Null
    @Schema(description = "Unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Size(max = 100)
    @Schema(description = "Serial number", example = "0ASUE8C00100KR")
    private String serialNumber;

    @Schema(description = "Model (weight class)", example = "LIGHTWEIGHT")
    private Model model;

    @Max(500)
    @Schema(description = "Weight limit for loading", example = "100")
    private Integer weightLimit;

    @NotNull
    @Max(100)
    @Schema(description = "Current battery capacity", example = "100")
    private Integer batteryCapacity;

    @Schema(description = "Current drone state", example = "IDLE")
    private State state;
}
