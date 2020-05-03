package org.gkk.bioshopapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "PriceHistory not found!")
public class PriceHishoryNotFoundException extends RuntimeException {

    private int status;

    public PriceHishoryNotFoundException() {
        this.status = 404;
    }

    public PriceHishoryNotFoundException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
