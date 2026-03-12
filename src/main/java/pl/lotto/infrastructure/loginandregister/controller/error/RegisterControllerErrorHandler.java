package pl.lotto.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.lotto.domain.loginandregister.UserAlreadyExistsException;

@ControllerAdvice
@CrossOrigin(origins = "http://localhost:3000")
public class RegisterControllerErrorHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public RegisterErrorResponse handleUserAlreadyExists(UserAlreadyExistsException e) {
        return new RegisterErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }
}
