package com.malihanan.compressor.services;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;

public interface CompressorService {
    File doAction(MultipartFile file, String action, String algo) throws IOException;
    void writeToOut(File file, ServletOutputStream out) throws IOException;
}
