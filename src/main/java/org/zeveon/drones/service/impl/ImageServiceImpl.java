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
import java.nio.file.Paths;
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
    public Resource get(String imagePath) throws MalformedURLException {
        return new UrlResource(Paths.get(imagePath).toUri());
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        if (file != null) {
            var originalFileName = Strings.isNotBlank(file.getOriginalFilename())
                    ? file.getOriginalFilename()
                    : file.getName();
            var fileName = UUID.randomUUID().toString().concat(UNDERSCORE).concat(originalFileName);
            var path = Paths.get(imagePath, fileName);
            Files.write(path, file.getBytes());
            return path.toString();
        } else {
            return null;
        }
    }

    @Override
    public String getContentType(MultipartFile file) {
        return file != null
                ? file.getContentType()
                : null;
    }
}