package com.br.flavioreboucassantos.quarkussocial.rest;

import com.br.flavioreboucassantos.quarkussocial.domain.repository.PostRepository;
import com.br.flavioreboucassantos.quarkussocial.domain.repository.UserRepository;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.ErrorResponse;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;

import java.util.Set;

public abstract class BaseResource {

    @Inject
    protected Validator validator;

    public BaseResource() {
    }

    /**
     * Not Null is Fail.
     *
     * @param dto
     * @param <T>
     * @return null if there is no ConstraintViolation
     */
    public <T> Response tryConstraintViolation(T dto) {
        final Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(dto);
        if (constraintViolationSet.isEmpty())
            return null;
        else
            return ErrorResponse
                    .createFromValidation(constraintViolationSet)
                    .withStatusCode(ErrorResponse.UNPROCESSABLE_ENTITY_STATUS);
    }

    /**
     * How to Uses:
     * <pre><code>
     * Model model;
     * if ((model = Model.tryFindById(id)) == null)
     *      return disappointedFindById().build();
     * </code></pre>
     *
     * @return Response.ResponseBuilder with Response.Status.NOT_FOUND
     */
    public Response.ResponseBuilder disappointedFindById() {
        return Response.status(Response.Status.NOT_FOUND);
    }

    /**
     *
     * @param id
     * @return true if id == null or <= 0
     */
    public boolean tryIdBadRequest(Long id) {
        return id == null || id <= 0;
    }

    /**
     *
     * @return Response.ResponseBuilder with Response.Status.BAD_REQUEST
     */
    public Response.ResponseBuilder disappointedIdBadRequest() {
        return Response.status(Response.Status.BAD_REQUEST);
    }

    /**
     *
     * @param id1
     * @param id2
     * @return true if id1.equals(id2)
     */
    public boolean tryIdConflict(Long id1, Long id2) {
        return id1.equals(id2);
    }

    /**
     *
     * @return Response.ResponseBuilder with Response.Status.CONFLICT
     */
    public Response.ResponseBuilder disappointedIdConflict() {
        return Response.status(Response.Status.CONFLICT);
    }

    /**
     *
     * @return Response.ResponseBuilder with Response.Status.NOT_MODIFIED
     */
    public Response.ResponseBuilder disappointedPersist() {
        return Response.status(Response.Status.NOT_MODIFIED); // without response body .entity("response body")
    }

    /**
     *
     * @return Response.ResponseBuilder with Response.Status.FORBIDDEN
     */
    public Response.ResponseBuilder disappointedFind() {
        return Response
                .status(Response.Status.FORBIDDEN);
    }


}
