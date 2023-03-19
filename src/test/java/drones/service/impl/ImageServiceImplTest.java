package drones.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.zeveon.drones.service.impl.ImageServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Stanislav Vafin
 */
class ImageServiceImplTest {

    private ImageServiceImpl imageService;

    @TempDir
    Path tempDir;

    public static Stream<Arguments> saveTestParams() {
        return Stream.of(
                arguments(
                        new MockMultipartFile("fileName.jpg", "testImage.jpg", "image/jpeg", "image-content".getBytes()),
                        "testImage.jpg"
                ),
                arguments(
                        new MockMultipartFile("fileName.jpg", "", "image/jpeg", "image-content".getBytes()),
                        "fileName.jpg"
                ),
                arguments(
                        new MockMultipartFile("fileName.jpg", null, "image/jpeg", "image-content".getBytes()),
                        "fileName.jpg"
                )
        );
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
        imageService = new ImageServiceImpl();
        setImagePath(imageService, tempDir.toString());
        imageService.init();
    }

    private void setImagePath(Object target, Object value) throws NoSuchFieldException, IllegalAccessException {
        var field = target.getClass().getDeclaredField("imagePath");
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void getAllFilePathsTest() throws IOException {
        var fileName1 = "testImage1.jpg";
        var fileName2 = "testImage2.jpg";
        Files.createFile(tempDir.resolve(fileName1));
        Files.createFile(tempDir.resolve(fileName2));

        var filePaths = imageService.getAllFilePaths();

        assertEquals(2, filePaths.size());
        assertTrue(filePaths.contains(tempDir.resolve(fileName1).toString()));
        assertTrue(filePaths.contains(tempDir.resolve(fileName2).toString()));
    }

    @Test
    void getAllFilePathsNegativeTest() throws NoSuchFieldException, IllegalAccessException {
        setImagePath(imageService, tempDir.resolve("non-existing").toString());

        assertThrows(RuntimeException.class, () -> imageService.getAllFilePaths());
    }

    @Test
    void getTest() throws IOException {
        var filePath = tempDir.resolve("testImage.jpg");
        Files.createFile(filePath);

        var resource = imageService.get(filePath.toString());

        assertNotNull(resource);
        assertEquals(filePath.toString(), resource.getFile().getPath());
    }

    @Test
    void getNegativeTest() throws NoSuchFieldException, IllegalAccessException {
        setImagePath(imageService, "invalid:url");

        assertThrows(RuntimeException.class, () -> imageService.get("invalid:url"));
    }

    @ParameterizedTest
    @MethodSource("saveTestParams")
    void saveTest(MockMultipartFile file, String expectedFileName) throws IOException {
        var savedFilePath = imageService.save(file);

        var savedPath = Path.of(savedFilePath);
        assertTrue(savedFilePath.contains(expectedFileName));
        assertTrue(Files.exists(savedPath));
        assertEquals("image-content", Files.readString(savedPath));
    }

    @Test
    void saveTestNull() {
        var savedFilePath = imageService.save(null);

        assertNull(savedFilePath);
    }

    @Test
    void saveNegativeTest() throws IOException {
        var file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("testImage.jpg");
        when(file.getName()).thenReturn("fileName.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> imageService.save(file));
    }

    @Test
    void removeTest() throws IOException {
        var fileName = "testImage.jpg";
        var filePath = tempDir.resolve(fileName);
        Files.createFile(filePath);

        imageService.remove(filePath.toString());

        assertFalse(Files.exists(filePath));
    }

    @Test
    void removeNegativeTest() {
        var nonExistingFilePath = tempDir.resolve("non-existing.jpg").toString();

        assertThrows(RuntimeException.class, () -> imageService.remove(nonExistingFilePath));
    }

    @Test
    void getContentTypeTest() {
        var file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");

        var contentType = imageService.getContentType(file);

        assertEquals("image/jpeg", contentType);
    }

    @Test
    void getContentTypeTestNull() {
        var contentType = imageService.getContentType(null);

        assertNull(contentType);
    }
}
