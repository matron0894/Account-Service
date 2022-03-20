package account.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "firstname can't be empty")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "lastname can't be empty")
    private String lastname;

    @Email
    @NotBlank(message = "email can't be empty")
    @Pattern(regexp = "\\w+(@acme.com)$",
            message = "Invalid email")
    private String email;

    @NotBlank(message = "password can't be empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


}
