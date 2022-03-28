package account.view;

import account.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserAdminRepresentation {

    private final Long id;
    private final String name;
    private final String lastname;
    private final String email;
    private final List<String> roles;

    public UserAdminRepresentation(User appUser) {
        this.id = appUser.getId();
        this.name = appUser.getName();
        this.lastname = appUser.getLastname();
        this.email = appUser.getEmail();
        this.roles = new ArrayList<>(appUser.getRoles());
    }
}