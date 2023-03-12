package org.zeveon.drones.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author Stanislav Vafin
 */
public interface ImageService {

    Resource get(String fileName) throws MalformedURLException;

    String save(MultipartFile file) throws IOException;

    String getContentType(MultipartFile file) throws IOException;
}
