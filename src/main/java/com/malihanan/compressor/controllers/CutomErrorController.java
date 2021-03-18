package com.malihanan.compressor.controllers;

import com.malihanan.compressor.exceptions.InvalidExtensionException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CutomErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String message = ((Exception)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)).getCause().getMessage();
        model.addAttribute("message", message);
        return "error";
    }
}
