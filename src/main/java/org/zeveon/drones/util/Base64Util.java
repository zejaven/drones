package org.zeveon.drones.util;

import org.zeveon.drones.model.MultipartFileInfo;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Stanislav Vafin
 */
public class Base64Util {

    public static final String EVERYTHING_PATTERN = ".*";
    public static final String COLON = ":";

    public static MultipartFileInfo convertCustomBase64ToBase64(String customBase64) {
        var fileName = customBase64.replaceFirst(COLON.concat(EVERYTHING_PATTERN), EMPTY);
        var contentType = customBase64.replaceFirst(fileName.concat(COLON), EMPTY)
                .replaceFirst(COLON.concat(EVERYTHING_PATTERN), EMPTY);
        var base64Representation = customBase64.replaceFirst(fileName.concat(COLON).concat(contentType).concat(COLON), EMPTY);
        return MultipartFileInfo.builder()
                .fileName(fileName)
                .contentType(contentType)
                .base64Representation(base64Representation)
                .build();
    }
}
