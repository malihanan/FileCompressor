package com.malihanan.compressor.services;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;

public interface CompressorService {
    File doAction(MultipartFile file, String action, String algo) throws IOException;
    void writeToOut(File file, ServletOutputStream out) throws IOException;
    public double getCompressionRatio(String action, MultipartFile in_file, File out_file);
}
