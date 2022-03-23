package account.model;

import account.validation.DateFormatValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Entity
public class Payment {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email
    @NotBlank(message = "Employee cannot be blank!")
    @Pattern(regexp = "\\w+(@acme.com)$",
            message = "Not a valid email!")
    private String employee;

// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-yyyy")
    @DateFormatValidation(message = "Wrong date!")
    @Column(name = "period", nullable = false/*, unique = true*/)
    private String period;


    @Min(value = 0L, message = "Salary must be non negative!")
    private Long salary;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
