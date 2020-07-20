package org.gkk.bioshopapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.USERNAME_NOT_FOUND;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = USERNAME_NOT_FOUND)
public class ProductDuplicateException extends RuntimeException {

    private int status;

    public ProductDuplicateException() {
        this.status = 400;
    }

    public ProductDuplicateException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
