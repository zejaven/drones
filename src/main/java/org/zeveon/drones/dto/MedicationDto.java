package org.zeveon.drones.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.zeveon.drones.component.Base64ToMultipartFileDeserializer;

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

    @JsonDeserialize(using = Base64ToMultipartFileDeserializer.class)
    private MultipartFile image;
}
