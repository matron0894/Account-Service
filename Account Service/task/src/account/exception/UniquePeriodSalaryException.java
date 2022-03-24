package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UniquePeriodSalaryException extends RuntimeException {

    public UniquePeriodSalaryException(String data) {
        super("Payment [" + data + "]: The period is repeated!");
    }
}
