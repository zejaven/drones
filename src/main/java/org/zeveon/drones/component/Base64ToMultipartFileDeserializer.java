package org.zeveon.drones.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Stanislav Vafin
 */
@Component
public class Base64ToMultipartFileDeserializer extends StdDeserializer<MultipartFile> {

    private static final String EVERYTHING_PATTERN = ".*";
    public static final String COLON = ":";

    public Base64ToMultipartFileDeserializer() {
        this(null);
    }

    public Base64ToMultipartFileDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MultipartFile deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var text = jsonParser.getText();
        var fileName = text.replaceFirst(COLON.concat(EVERYTHING_PATTERN), EMPTY);
        var contentType = text.replaceFirst(fileName.concat(COLON), EMPTY)
                .replaceFirst(COLON.concat(EVERYTHING_PATTERN), EMPTY);
        var base64image = text.replaceFirst(fileName.concat(COLON).concat(contentType).concat(COLON), EMPTY);
        return new MockMultipartFile(fileName, fileName, contentType, Base64.getDecoder().decode(base64image));
    }
}
