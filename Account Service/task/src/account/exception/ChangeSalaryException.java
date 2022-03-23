package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Salary don't change! Check input data!")
public class ChangeSalaryException extends RuntimeException {
}
