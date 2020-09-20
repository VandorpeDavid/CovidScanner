package be.winagent.covidscanner.controllers;

import be.winagent.covidscanner.controllers.annotations.Required;
import be.winagent.covidscanner.controllers.forms.converter.BidirectionalConverter;
import be.winagent.covidscanner.controllers.forms.models.EventForm;
import be.winagent.covidscanner.domain.models.*;
import be.winagent.covidscanner.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/events")
@AllArgsConstructor
public class EventController extends CovidScannerController {
    private final EventService eventService;
    private final BidirectionalConverter<Event, EventForm> eventFormConverter;

    @GetMapping("/show")
    @PreAuthorize("isEventAdmin(#event)")
    public String show(@Required Event event) {
        return "events/show";
    }

    @GetMapping("/create")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String createForm(@Required Association association, EventForm eventForm) {
        return "events/create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAssociationAdmin(#association)")
    public String create(@Valid EventForm eventForm, BindingResult bindingResult, @Required Association association) {
        if (bindingResult.hasErrors()) {
            return "events/create";
        }

        Event event = eventFormConverter.build(eventForm);
        event.setAssociation(association);
        event = eventService.create(event);
        return redirect(event);
    }

    @GetMapping("/edit")
    @PreAuthorize("isEventAdmin(#event)")
    public String editForm(Model model, Event event) {
        model.addAttribute("eventForm", eventFormConverter.reverseBuild(event));
        return "events/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("isEventAdmin(#event)")
    public String edit(@Valid EventForm eventForm, BindingResult bindingResult, @Required Event event) {
        if (bindingResult.hasErrors()) {
            return "events/edit";
        }

        event = eventService.update(eventFormConverter.update(event, eventForm));
        return redirect(event);
    }

    public String redirect(Event event) {
        return redirect(event, "show");
    }

    public String redirect(Event event, String action) {
        return String.format("redirect:/events/%s?event=%s", action, event.getId());
    }
}
