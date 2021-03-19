package com.malihanan.compressor.controllers;

import com.malihanan.compressor.services.CompressorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class MainController {

    private final CompressorService compressorService;

    double ratio;

    @ModelAttribute("ratio")
    public String ratio() {
        return String.format("%.2f", ratio);
    }

    @Autowired
    public MainController(CompressorService compressorService) {
        this.compressorService = compressorService;
    }

    @GetMapping("/")
    public String upload(Model model) {
        return "uploadForm";
    }

    @PostMapping("/")
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("algo") String algo,
                                 @RequestParam("action") String action,
                                 Model model,
                                 HttpServletResponse response) throws IOException {

        File out_file = compressorService.doAction(file, action, algo);
        ratio = compressorService.getCompressionRatio(action, file, out_file);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + out_file.getName() + "\"");
        compressorService.writeToOut(out_file, response.getOutputStream());
    }
}
