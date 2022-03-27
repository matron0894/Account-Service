package account.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
public class ChangeUserRoleRepresentation {

    @NotBlank
    private String user;

    @NotBlank
    private String role;

    @NotBlank
    private String operation;

}
