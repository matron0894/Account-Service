package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User exist!")
public class UserExistException extends RuntimeException {

//    public UsernameNotFoundException() {
//        super("User exist!");
//    }

}
