package account.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LengthConstraintValidator.class)
public @interface LengthConstraintValidation {
    String message() default "Password length must be 12 chars minimum!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}