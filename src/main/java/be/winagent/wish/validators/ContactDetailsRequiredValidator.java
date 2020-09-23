package be.winagent.wish.validators;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactDetailsRequiredValidator implements ConstraintValidator<ContactDetailsRequired, HasContactDetails> {
    @Override
    public boolean isValid(HasContactDetails hasContactDetails, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = hasContactDetails == null
                || !(
                StringUtils.isEmpty(hasContactDetails.getEmail())
                        && StringUtils.isEmpty(hasContactDetails.getPhone())
        );
        if(! valid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                        .addPropertyNode( "phone" ).addConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode( "email" ).addConstraintViolation();
        }
        return valid;
    }
}
