package be.winagent.covidscanner.controllers.forms.converter;

import be.winagent.covidscanner.controllers.forms.models.AssociationForm;
import be.winagent.covidscanner.domain.models.Association;
import org.springframework.stereotype.Component;

@Component
public class AssociationConverter implements BidirectionalConverter<Association, AssociationForm> {
    @Override
    public AssociationForm reverseUpdate(AssociationForm target, Association source) {
        target.setAbbreviation(source.getAbbreviation());
        target.setName(source.getName());
        return target;
    }

    @Override
    public AssociationForm reverseBuild(Association source) {
        return reverseUpdate(new AssociationForm(), source);
    }

    @Override
    public Association update(Association target, AssociationForm source) {
        target.setAbbreviation(source.getAbbreviation());
        target.setName(source.getName());
        return target;
    }

    @Override
    public Association build(AssociationForm associationForm) {
        return update(new Association(), associationForm);
    }
}
