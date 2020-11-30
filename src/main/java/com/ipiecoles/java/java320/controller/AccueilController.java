package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {

    @Autowired
    private EmployeRepository employeRepository;

    // Pour test : http://localhost:8080
    @GetMapping(value = "/")
    public String index(final ModelMap model){
        model.put("count", employeRepository.count());
        return "accueil";
    }

}
