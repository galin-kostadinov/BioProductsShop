package org.gkk.bioshopapp.error;

import org.gkk.bioshopapp.constant.ErrorMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.*;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = CATEGORY_NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {

    private int status;

    public CategoryNotFoundException() {
        this.status = 404;
    }

    public CategoryNotFoundException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
