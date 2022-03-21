package account.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/* Actual implementation of logic for @BreachedPasswordValidation annotation */
public class BreachedPasswordValidator implements ConstraintValidator<BreachedPasswordValidation, String> {

    public boolean isValid(String emailName, ConstraintValidatorContext cxt) {
        List<String> list = Arrays.asList("PasswordForJanuary",
                "PasswordForFebruary",
                "PasswordForMarch",
                "PasswordForApril",
                "PasswordForMay",
                "PasswordForJune",
                "PasswordForJuly",
                "PasswordForAugust",
                "PasswordForSeptember",
                "PasswordForOctober",
                "PasswordForNovember",
                "PasswordForDecember");
        return !list.contains(emailName);
    }
}
