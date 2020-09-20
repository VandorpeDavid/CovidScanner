package be.fkgent.election.controllers;

import be.fkgent.election.controllers.annotations.Required;
import be.fkgent.election.controllers.forms.converter.BidirectionalConverter;
import be.fkgent.election.controllers.forms.models.AssociationForm;
import be.fkgent.election.domain.models.Association;
import be.fkgent.election.domain.models.User;
import be.fkgent.election.services.AssociationService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/associations")
@AllArgsConstructor
public class AssociationController extends FkElectionController {
    private final AssociationService associationService;
    private final BidirectionalConverter<Association, AssociationForm> associationConverter;

    @GetMapping
    @PreAuthorize("isAdmin()")
    public String index(Model model) {
        model.addAttribute("associations", associationService.all());
        return "associations/index";
    }

    @GetMapping("/show")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String show(@Required Association association) {
        return "associations/show";
    }

    @GetMapping("/create")
    @PreAuthorize("isAdmin()")
    public String createForm(AssociationForm associationForm) {
        return "associations/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAdmin()")
    public String create(@Valid AssociationForm associationForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "associations/create";
        }

        Association association = associationService.create(associationConverter.build(associationForm));
        return redirect(association);
    }

    @PostMapping("/addAdmin")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String addAdmin(@Required Association association, @RequestParam String username) {
        associationService.addAdmin(association, username);
        return redirect(association);
    }

    @PostMapping("/deleteAdmin")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String deleteAdmin(@Required Association association, @RequestParam String username, @ModelAttribute(name = "currentUser") User currentUser) {
        if(currentUser.getUsername().equals(username)) {
            return redirect(association);
        }
        associationService.removeAdmin(association, username);
        return redirect(association);
    }

    private String redirect(Association association) {
        return String.format("redirect:/associations/show?association=%s", association.getAbbreviation());
    }
}
