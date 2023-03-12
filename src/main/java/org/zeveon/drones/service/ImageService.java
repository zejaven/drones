package org.zeveon.drones.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Stanislav Vafin
 */
public interface ImageService {

    String save(MultipartFile file) throws IOException;
}
