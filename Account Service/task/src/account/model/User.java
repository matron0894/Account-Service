package account.model;

import account.validation.BreachedPasswordValidation;
import account.validation.LengthConstraintValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
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


    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"
            ))
    @SortNatural
    private SortedSet<Group> roles = new TreeSet<>();

}
