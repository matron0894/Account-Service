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
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
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
    //@SortNatural
    private Set<String> roles = new HashSet<>();


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
