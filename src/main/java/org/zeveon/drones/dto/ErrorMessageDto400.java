package org.zeveon.drones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageDto400 {

    @Schema(description = "Date and time when error happened", example = "2023-03-18T10:38:38.5226915")
    private LocalDateTime timestamp;

    @Schema(description = "Response status", example = "400")
    private int status;

    @Schema(description = "Path to api method where error happened", example = "/api/drones/register")
    private String path;

    @Schema(description = "Exception name", example = "org.springframework.web.bind.MethodArgumentNotValidException")
    private String exception;

    @Schema(description = "Exception message", example = "Validation failed for argument [0] in ...")
    private String message;

    @Schema(description = "List of error details", example = """
                [
                    "batteryCapacity: 'должно быть не больше 100'",
                    "weightLimit: 'должно быть не больше 500'"
                ]
            """)
    private List<String> details;
}
