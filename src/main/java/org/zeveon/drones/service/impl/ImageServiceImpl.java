package org.zeveon.drones.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zeveon.drones.service.ImageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * @author Stanislav Vafin
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final String UNDERSCORE = "_";

    @Value("${drones.medication.images.path}")
    private String imagePath;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(imagePath).toAbsolutePath().normalize());
    }

    @Override
    public List<String> getAllFilePaths() {
        try (var paths = Files.walk(Paths.get(imagePath))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource get(String imagePath) {
        try {
            return new UrlResource(Paths.get(imagePath).toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String save(MultipartFile file) {
        if (file != null) {
            var originalFileName = Strings.isNotBlank(file.getOriginalFilename())
                    ? file.getOriginalFilename()
                    : file.getName();
            var fileName = UUID.randomUUID().toString().concat(UNDERSCORE).concat(originalFileName);
            var path = Paths.get(imagePath, fileName);
            try {
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return path.toString();
        } else {
            return null;
        }
    }

    @Override
    public void remove(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getContentType(MultipartFile file) {
        return file != null
                ? file.getContentType()
                : null;
    }
}
