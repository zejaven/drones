package org.zeveon.drones.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Stanislav Vafin
 */
public interface ImageService {

    List<String> getAllFilePaths();

    Resource get(String fileName);

    String save(MultipartFile file);

    void remove(String filePath);

    String getContentType(MultipartFile file);
}
