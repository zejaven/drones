package org.zeveon.drones.dto;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Stanislav Vafin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDto {

    @Null
    private Long id;

    private String name;

    private Integer weight;

    private String code;
}
