package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyEntityNotFoundException extends RuntimeException {

    public MyEntityNotFoundException() {
        super("User exist!");
    }

    public MyEntityNotFoundException(String name){
        super("Email not found");
    }
}
