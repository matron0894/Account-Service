package account.model;

import account.validation.BreachedPasswordValidation;
import account.validation.LengthConstraintValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "The name can't be empty")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "The lastname can't be empty")
    private String lastname;

    @Email
    @NotBlank(message = "Email cannot be blank!")
    @Pattern(regexp = ".*@acme.com$",
            message = "Not a valid email!")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "The password can't be empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @LengthConstraintValidation
    @BreachedPasswordValidation
    private String password;


    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "failed_attempt")
    private int failedAttempt;

    @Column(name = "lock_time")
    private LocalDate lockTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    @SortNatural
    private List<String> roles = new ArrayList<>(4);

    public void addRole(String role) {
        roles.add(role);
    }

    public void removeRole(String role) {
        roles.remove(role);
    }

    public void modifyRole(String operation, String role) {
        if (operation.equals("GRANT")) {
            addRole(role);
        }
        if (operation.equals("REMOVE")) {
            removeRole(role);
        }
        this.roles.sort(Comparator.naturalOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
