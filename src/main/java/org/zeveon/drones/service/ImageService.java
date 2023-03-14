package org.zeveon.drones.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Stanislav Vafin
 */
public interface ImageService {

    Resource get(String fileName);

    String save(MultipartFile file);

    String getContentType(MultipartFile file);
}
