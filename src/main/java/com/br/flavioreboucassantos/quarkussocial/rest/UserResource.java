package com.br.flavioreboucassantos.quarkussocial.rest;

import com.br.flavioreboucassantos.quarkussocial.domain.model.User;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.RequestUserResource;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource extends BaseResource {

    @POST
    @Transactional
    public Response createUser(RequestUserResource requestUserResource) {
        Response response;

        if ((response = tryConstraintViolation(requestUserResource)) != null)
            return response;

        User user = new User();
        user.setAge(requestUserResource.getAge());
        user.setName(requestUserResource.getName());
        user.persist();

        response = Response
                .status(Response.Status.CREATED)
                .entity(user)
                .build();

        return response;
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> users = User.findAll();

        return Response.ok(users.list()).build();
    }

    @DELETE
    @Path("{id}") //-> /users/3
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        User user;
        if ((user = User.tryFindById(id)) == null)
            return disappointedFindById()
                    .entity("deleteUser-id-NOT_FOUND")
                    .build();

        user.delete();
        return Response.noContent().build();

    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, RequestUserResource requestUserResource) {
        Response response;
        if ((response = tryConstraintViolation(requestUserResource)) != null)
            return response;

        User user;
        if ((user = User.tryFindById(id)) == null)
            return disappointedFindById()
                    .entity("updateUser-id-NOT_FOUND")
                    .build();

        user.setName(requestUserResource.getName());
        user.setAge(requestUserResource.getAge());

        return Response.noContent().build();
    }

}
