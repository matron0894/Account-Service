package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email not found")
public class EmailNotFoundException extends RuntimeException {

//    public EmailNotFoundException() {
//        super("Email not found");
//    }
}
