package account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Your account has been unlocked. Please try to login again.")
public class LockedException extends RuntimeException{
}
