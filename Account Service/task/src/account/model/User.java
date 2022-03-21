package account.model;

import account.validation.BreachedPasswordValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

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
    @Pattern(regexp = "\\w+(@acme.com)$",
            message = "Not a valid email!")
    private String email;

    @NotBlank(message = "The password can't be empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 12, message = "The password length must be at least 12 chars!")
    @BreachedPasswordValidation(message = "The password is in the hacker's database!")
    private String password;



}
