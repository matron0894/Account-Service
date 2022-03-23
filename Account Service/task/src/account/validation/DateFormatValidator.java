package account.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateFormatValidator implements ConstraintValidator<DateFormatValidation, String> {
    @Override
    public boolean isValid(String date, ConstraintValidatorContext cxt) {
       return date.matches("^((0?[1-9]|1[012])-\\d{4})$");
    }
}
