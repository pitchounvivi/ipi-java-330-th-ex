package com.ipiecoles.java.java320.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    // Pour test : http://localhost:8080/test
    @GetMapping(value = "/test")
    public String test(final ModelMap model) {
        model.put("nom", "IPI");
        return "test";
    }

}
