package org.gkk.bioshopapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Category not found!")
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
