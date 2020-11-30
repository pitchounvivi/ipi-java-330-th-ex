package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/employes") //indication pour les routes
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    //Recherche d'un employé par son id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET)
    public String getEmployeById(
            final ModelMap model,
            @PathVariable Long id
    ){
        Optional<Employe> employeOptional = employeRepository.findById(id);

        //pour plus tard gestion erreur 404

        model.put("employe", employeOptional.get());
        return "detail";
    }


    //Recherche d'un employé par son matricule
    @RequestMapping(
            params = "matricule",
            method = RequestMethod.GET)
    public String searchByMatricule(
            final ModelMap model,
            @RequestParam(value = "matricule") String matricule
    ){
        Employe employe = employeRepository.findByMatricule(matricule);

        //gérer 404

        model.put("employe", employe);
        return "detail";
    }


    //Liste des employés
    @RequestMapping(
            params = {"page","size","sortProperty","sortDirection"},
            method = RequestMethod.GET
    )
    public String listDeTousLesEmployes(
            final ModelMap model,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){
        Page<Employe> pageEmployes = employeRepository.findAll(PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty));

//        pageEmployes.getTotalElements();
//        pageEmployes.getTotalPages();

        model.put("employes", pageEmployes);
        return "listeEmployes";
    }


}
