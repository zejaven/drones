package org.zeveon.drones.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static org.zeveon.drones.util.Base64Util.convertCustomBase64ToBase64;

/**
 * @author Stanislav Vafin
 */
@Component
public class Base64ToMultipartFileDeserializer extends StdDeserializer<MultipartFile> {

    public Base64ToMultipartFileDeserializer() {
        this(null);
    }

    public Base64ToMultipartFileDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MultipartFile deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var multipartFileInfo = convertCustomBase64ToBase64(jsonParser.getText());
        return new MockMultipartFile(
                multipartFileInfo.getFileName(),
                multipartFileInfo.getFileName(),
                multipartFileInfo.getContentType(),
                Base64.getDecoder().decode(multipartFileInfo.getBase64Representation()));
    }
}
