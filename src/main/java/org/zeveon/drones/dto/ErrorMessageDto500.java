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
public class ErrorMessageDto500 {

    @Schema(description = "Date and time when error happened", example = "2023-03-18T10:58:17.6822453")
    private LocalDateTime timestamp;

    @Schema(description = "Response status", example = "500")
    private int status;

    @Schema(description = "Path to api method where error happened", example = "/api/drones/55/load-medication")
    private String path;

    @Schema(description = "Exception name", example = "java.lang.RuntimeException")
    private String exception;

    @Schema(description = "Exception message", example = "There is no drone with id: 55")
    private String message;

    @Schema(description = "List of error details", example = """
                [
                    null
                ]
            """)
    private List<String> details;
}
