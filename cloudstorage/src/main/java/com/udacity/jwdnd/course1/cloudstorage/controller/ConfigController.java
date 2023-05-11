package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfigController implements ErrorController {

    @GetMapping(path = "/error")
    public String errorPage() {
        return "404.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
