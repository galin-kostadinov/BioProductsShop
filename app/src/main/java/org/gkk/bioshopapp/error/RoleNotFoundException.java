package org.gkk.bioshopapp.error;

import org.gkk.bioshopapp.constant.ErrorMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.*;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = ROLE_NOT_FOUND)
public class RoleNotFoundException extends RuntimeException {

    private int status;

    public RoleNotFoundException() {
        this.status = 404;
    }

    public RoleNotFoundException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
