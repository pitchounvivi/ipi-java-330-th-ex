package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Commercial;
import com.ipiecoles.java.java320.model.Technicien;
import com.ipiecoles.java.java320.model.Manager;
import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
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
        if(employeOptional.isEmpty()){
            throw new EntityNotFoundException("L'employé d'identifiant " + id + " n'a pas été trouvé !");
        }

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
            @RequestParam(defaultValue = "matricule") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){
        Page<Employe> pageEmployes = employeRepository.findAll(PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty));

//        pageEmployes.getTotalElements();
//        pageEmployes.getTotalPages();

        model.put("employes", pageEmployes);

        //Affichage du numéro des éléments présents dans la page
        model.put("start", page * size +1);
        model.put("end", (page) * size + pageEmployes.getNumberOfElements());

        //Les boutons et l'affichage de la page en cours
        model.put("pageNumber", page);//C'est pour afficher la page en cours
        model.put("previousPage", page-1);
        model.put("nextPage", page+1);

        return "listeEmployes";
    }


    ////////////////////////////////////Zone de gestion de formulaire
    //Accéder au formulaire de création d'un employé
    @RequestMapping(
            value = "/new/{typeEmploye}",
            method = RequestMethod.GET
    )
    public String newEmploye(@PathVariable String typeEmploye, final ModelMap model){
       switch (typeEmploye.toLowerCase()){
           case "technicien": model.put("employe", new Technicien()); break;
           case "commercial": model.put("employe", new Commercial()); break;
           case "manager": model.put("employe", new Manager()); break;
       }
        return "detail";
    }
    //==> dans le template, il affiche le salaire de base par défaut => c'est normal



    //Création et enregistrement d'un commercial
    @RequestMapping(method = RequestMethod.POST, value = "/commercial",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveCommercial(Commercial employe){
        return saveEmploye(employe);
    }

    //Création et enregistrement d'un technicien
    @RequestMapping(method = RequestMethod.POST, value = "/technicien",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveTechnicien(Technicien employe){
        return saveEmploye(employe);
    }

    //Création et enregistrement d'un manager
    @RequestMapping(method = RequestMethod.POST, value = "/manager",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveManager(Manager employe){
        return saveEmploye(employe);
    }

    //Redirection après l'enregistrement pour avoir une URL correcte et pas une demande de renvoi de formulaire
    private RedirectView saveEmploye(Employe employe){
        employe = employeRepository.save(employe);
        return new RedirectView("/employes/" + employe.getId());
    }



    //Supression d'un employé
//    @RequestMapping(value = "/{id}/delete",
//            method = RequestMethod.GET)
//    public RedirectView deleteEmploye(
//            @PathVariable Long id
//    ){
//        //Faire la suppression
//        employeRepository.deleteById(id);
//
//        //Quand c'est fait on redirige
//        return new RedirectView("/employes");
//    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public RedirectView deleteEmploye(@PathVariable Long id){
        if(!employeRepository.existsById(id)){
            throw new EntityNotFoundException("L'employé d'identifiant " + id + " n'a pas été trouvé !");
        }
        employeRepository.deleteById(id);
        return new RedirectView("/employes?page=0&size=10&sortProperty=matricule&sortDirection=ASC");
    }

}
