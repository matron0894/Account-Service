package account.view;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestAccessRepresentation {

    @NotBlank
    private String user;

    @NotBlank
    private String operation;
}
