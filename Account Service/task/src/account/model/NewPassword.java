package account.model;

import account.validation.BreachedPasswordValidation;
import account.validation.LengthConstraintValidation;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewPassword {

    @NotBlank(message = "Password cannot be null or blank")
    @LengthConstraintValidation
    @BreachedPasswordValidation
    private String new_password;
}
