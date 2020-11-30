package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
@RequestMapping("/employes") //indication pour les routes
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEmployeById(
            final ModelMap model,
            @PathVariable Long id
    ){
        
    }

}
