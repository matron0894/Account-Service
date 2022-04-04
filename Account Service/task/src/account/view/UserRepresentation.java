package account.view;

import account.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.TreeSet;

@Getter
@AllArgsConstructor
public class UserRepresentation {

    private final Long id;
    private final String name;
    private final String lastname;
    private final String email;
    private final Set<String> roles;

    public UserRepresentation(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        Set<String> tmp = new TreeSet<>();
        Set<String> roles = user.getRoles();
        for (String s : roles) {
            tmp.add("ROLE_" + s);
        }
        this.roles = tmp;
    }
}