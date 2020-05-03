package org.gkk.bioshopapp.error;

import org.gkk.bioshopapp.constant.ErrorMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.*;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = PRODUCT_NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    private int status;

    public ProductNotFoundException() {
        this.status = 404;
    }

    public ProductNotFoundException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
