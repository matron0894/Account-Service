package account.model;

import account.validation.BreachedPasswordValidation;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ChangePass {

    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @BreachedPasswordValidation(message = "The password is in the hacker's database!")
    private String new_password;
}
