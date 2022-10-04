package com.example.eduwithbe;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String redirectRoot() {

        return "index.html";
    }

    public String getErrorPath() {

        return "/error";
    }
}