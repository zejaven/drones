package org.zeveon.drones.dto;

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
public class ErrorMessageDto {

    private LocalDateTime timestamp;

    private int status;

    private String path;

    private String exception;

    private String message;

    private List<String> details;
}
