package be.fkgent.election.controllers;

import be.fkgent.election.domain.models.Association;
import be.fkgent.election.domain.models.User;
import be.fkgent.election.security.annotations.Authenticated;
import be.fkgent.election.services.AssociationService;
import be.fkgent.election.services.implementation.LDAPUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class HomeController extends FkElectionController {
    private final AssociationService associationService;
    private final LDAPUserService userRepository;

    @GetMapping("/")
    @Authenticated
    public String home(@ModelAttribute(name = "currentUser") User currentUser, Model model) {
        if (currentUser.isAdmin()) {
            return "redirect:/associations";
        }

        List<Association> adminAssociations = associationService.all()
                .stream()
                .filter((association) -> association.getAdmins().contains(currentUser))
                .collect(Collectors.toList());

        if (adminAssociations.size() == 1) {
            return "redirect:/associations/show?association=" + adminAssociations.get(0).getAbbreviation();
        }

        model.addAttribute("adminAssociations", adminAssociations);
        return "home";
    }
}
