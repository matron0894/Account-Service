package account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@AllArgsConstructor
public class UserPaymentRepresentation {
    private String name;
    private String lastname;
    private String period;
    private String salary;
}
