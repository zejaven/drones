package org.zeveon.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Pattern(regexp = "^[A-Za-z\\d-_]+$")
    @Schema(description = "Name", example = "Aspirin")
    private String name;

    @Schema(description = "Weight", example = "10")
    private Integer weight;

    @Pattern(regexp = "^[A-Z\\d_]+$")
    @Schema(description = "Code", example = "AP01_075")
    private String code;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = Base64ToMultipartFileDeserializer.class)
    private MultipartFile image;

    @Schema(description = "ID of the drone the medication is loaded", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long droneId;
}
