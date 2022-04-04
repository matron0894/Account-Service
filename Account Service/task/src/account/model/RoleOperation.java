package account.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleOperation {

    private String user;
    private String role;
    private String operation;
}
