package account.view;

import account.validation.BreachedPasswordValidation;
import account.validation.LengthConstraintValidation;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class NewPassword {

    @NotBlank(message = "Password cannot be null or blank")
    @LengthConstraintValidation
    @BreachedPasswordValidation
    private String new_password;
}
