package be.winagent.covidscanner.controllers.forms.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EventForm {
    @NotEmpty
    private String name;
}
