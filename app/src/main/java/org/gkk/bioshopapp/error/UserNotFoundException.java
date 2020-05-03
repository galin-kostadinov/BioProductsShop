package org.gkk.bioshopapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.USERNAME_NOT_FOUND;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = USERNAME_NOT_FOUND)
public class  UserNotFoundException extends RuntimeException {

    private int status;

    public UserNotFoundException() {
        this.status = 404;
    }

    public UserNotFoundException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
