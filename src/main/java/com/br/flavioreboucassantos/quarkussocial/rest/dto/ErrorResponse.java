package com.br.flavioreboucassantos.quarkussocial.rest.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ErrorResponse {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;

    private String message;
    private Collection<ErrorField> errors;

    public ErrorResponse(String message, Collection<ErrorField> errors) {
        this.message = message;
        this.errors = errors;
    }

    public Response withStatusCode(int code) {
        return Response.status(code).entity(this).build();
    }


    public static <T> ErrorResponse createFromValidation(Set<ConstraintViolation<T>> constraintViolationSet) {
        List<ErrorField> errors = constraintViolationSet
                .stream()
                .map(
                        constraintViolation -> new ErrorField(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage())
                )
                .collect(Collectors.toList());

        String message = "Validation Error";
        return new ErrorResponse(message, errors);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Collection<ErrorField> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ErrorField> errors) {
        this.errors = errors;
    }
}
