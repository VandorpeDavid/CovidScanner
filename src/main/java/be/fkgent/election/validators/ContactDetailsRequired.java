package be.fkgent.election.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ContactDetailsRequiredValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ContactDetailsRequired {
    String message() default "Need contact details.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
