package account.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class APIException {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
    private String message;
    private String path;

    public APIException(LocalDateTime timestamp, HttpStatus status, String error, String message, WebRequest request) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = request.toString();
    }
}
