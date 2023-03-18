package org.zeveon.drones.model;

import lombok.*;

/**
 * @author Stanislav Vafin
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipartFileInfo {

    private String fileName;

    private String contentType;

    private String base64Representation;
}
