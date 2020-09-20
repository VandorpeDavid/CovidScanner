package be.winagent.covidscanner.controllers.forms.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class AssociationForm {

    @NotEmpty
    @Length(max = 255)
    private String abbreviation;

    @NotEmpty
    @Length(max = 255)
    private String name;
}
