package rent.your.room.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import rent.your.room.dto.MessageResponse;
import rent.your.room.exception.TokenRefreshException;

import java.util.Date;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new MessageResponse(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }
}