package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST /*, reason = "User exist!"*/)
public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException() {
        super("User exist!");
    }

    public UsernameNotFoundException(String name) {
        super("Email not found");
    }
}
