package drones.component;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeveon.drones.component.Base64ToMultipartFileDeserializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Stanislav Vafin
 */
class Base64ToMultipartFileDeserializerTest {

    private Base64ToMultipartFileDeserializer deserializer;

    @BeforeEach
    void setUp() {
        deserializer = new Base64ToMultipartFileDeserializer();
    }

    @Test
    void testDeserialize() throws IOException {
        var fileName = "imageFile.jpg";
        var contentType = "image/jpeg";
        var content = "image-content";
        var base64Content = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
        var base64String = String.format("%s:%s:%s", fileName, contentType, base64Content);

        var parser = mock(JsonParser.class);
        when(parser.getText()).thenReturn(base64String);

        var result = deserializer.deserialize(parser, null);

        assertNotNull(result);
        assertEquals(fileName, result.getOriginalFilename());
        assertEquals(contentType, result.getContentType());
        assertEquals(content, new String(result.getBytes(), StandardCharsets.UTF_8));
    }
}
