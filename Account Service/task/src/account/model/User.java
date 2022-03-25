package account.model;

import account.validation.BreachedPasswordValidation;
import account.validation.LengthConstraintValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
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


    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<String> roles;


}
