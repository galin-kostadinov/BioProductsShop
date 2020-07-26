package org.gkk.bioshopapp.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Throwable.class})
    public ModelAndView handleException(Throwable exception) {
        ModelAndView modelAndView = new ModelAndView("error/custom-error");

        String message = exception.getMessage();

        modelAndView.addObject("message", message);

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView handleValidationExceptions(BindException ex) {
        ModelAndView modelAndView = new ModelAndView("error/bind-exception");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        modelAndView.addObject("errors", errors);
        return modelAndView;
    }
}
