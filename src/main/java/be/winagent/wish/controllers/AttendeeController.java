package be.winagent.wish.controllers;

import be.winagent.wish.controllers.annotations.Required;
import be.winagent.wish.controllers.exceptions.NotFoundException;
import be.winagent.wish.controllers.forms.converter.ExternalAttendeeFormConverter;
import be.winagent.wish.controllers.forms.models.ExternalAttendeeForm;
import be.winagent.wish.controllers.messages.AttendeeData;
import be.winagent.wish.controllers.messages.AttendeeScan;
import be.winagent.wish.domain.models.Event;
import be.winagent.wish.domain.models.User;
import be.winagent.wish.services.EventService;
import be.winagent.wish.services.UserService;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendees")
@AllArgsConstructor
public class AttendeeController extends CovidScannerController {
    private final EventService eventService;
    private final UserService userService;
    private final ExternalAttendeeFormConverter externalAttendeeFormConverter;

    @GetMapping
    @PreAuthorize("isEventAdmin(#event)")
    public String index(@Required Event event) {
        return "attendees/index";
    }

    @GetMapping("/export")
    @PreAuthorize("isEventAdmin(#event)")
    public void export(@Required Event event, HttpServletResponse response) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        //set file name and content type
        String filename = "users.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<AttendeeData> writer = new StatefulBeanToCsvBuilder<AttendeeData>(response.getWriter())
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        writer.write(
                event.getAttendees().stream()
                        .map(AttendeeData::new)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/scan")
    @PreAuthorize("isEventAdmin(#event)")
    public String createForm(@Required Event event) {
        return "attendees/scan";
    }

    @PostMapping(value = "/scan", produces = "application/json")
    @PreAuthorize("isEventAdmin(#event)")
    @ResponseBody
    public AttendeeScan createForm(@Required Event event, String barcode) {
        AttendeeScan scan = new AttendeeScan();
        User user = userService.findOrCreateUserByBarcode(barcode).orElseThrow(NotFoundException::new);
        scan.setAttendee(new AttendeeData(user));
        boolean changed = eventService.register(event, user);

        scan.setDuplicate(!changed);
        return scan;
    }

    @GetMapping("/createExtern")
    @PreAuthorize("isEventAdmin(#event)")
    public String createForm(@Required Event event, ExternalAttendeeForm externalAttendeeForm) {
        return "attendees/createExtern";
    }

    @PostMapping("/createExtern")
    @PreAuthorize("isEventAdmin(#event)")
    public String create(
            Model model,
            RedirectAttributes redirectAttributes,
            @Required Event event,
            @Valid ExternalAttendeeForm externalAttendeeForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("externalRegistration", "failure");
            return "attendees/createExtern";
        }

        eventService.register(event, externalAttendeeFormConverter.build(externalAttendeeForm));
        redirectAttributes.addFlashAttribute("externalRegistration", "success");
        return redirect(event, "createExtern");
    }

    public String redirect(Event event, String action) {
        return String.format("redirect:/attendees/%s?event=%s", action, event.getId());
    }
}
