package org.zeveon.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^[A-Za-z\\d-_]+$")
    private String name;

    private Integer weight;

    @Pattern(regexp = "^[A-Z\\d_]+$")
    private String code;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = Base64ToMultipartFileDeserializer.class)
    private MultipartFile image;

    private Long droneId;
}
