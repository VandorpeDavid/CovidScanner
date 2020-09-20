package be.fkgent.election.controllers.forms.converter;

import be.fkgent.election.controllers.forms.models.AssociationForm;
import be.fkgent.election.domain.models.Association;
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
