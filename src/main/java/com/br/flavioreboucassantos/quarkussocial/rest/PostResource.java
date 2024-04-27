package com.br.flavioreboucassantos.quarkussocial.rest;

import com.br.flavioreboucassantos.quarkussocial.domain.model.Post;
import com.br.flavioreboucassantos.quarkussocial.domain.model.RelationshipFollower;
import com.br.flavioreboucassantos.quarkussocial.domain.model.User;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.RequestPostResource;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.ResponsePost;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/users/{id}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource extends BaseResource {

    @POST
    @Transactional
    public Response createPost(@PathParam("id") Long id, RequestPostResource requestPostResource) {
        Response response;
        if ((response = tryConstraintViolation(requestPostResource)) != null)
            return response;

        User user;
        if ((user = User.tryFindById(id)) == null)
            return disappointedFindById()
                    .entity("createPost-id-NOT_FOUND")
                    .build();

        Post post = new Post();
        post.setPostText(requestPostResource.getPostText());
        post.setUser(user);
        post.persist();

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("id") Long id, @HeaderParam("idFollower") Long idFollower) {
        User user;
        if ((user = User.tryFindById(id)) == null)
            return disappointedFindById()
                    .entity("listPosts-id-NOT_FOUND")
                    .build();

        if (tryIdBadRequest(idFollower))
            return disappointedIdBadRequest()
                    .entity("listPosts-idFollower-BAD_REQUEST")
                    .build();

        if (tryIdConflict(id, idFollower))
            return disappointedIdConflict()
                    .entity("listPosts-idFollower-CONFLICT")
                    .build();

        User userFollower;
        if ((userFollower = User.tryFindById(idFollower)) == null)
            return disappointedFindById()
                    .entity("listPosts-idFollower-NOT_FOUND")
                    .build();

        PanacheQuery<?> query;
        if ((query = RelationshipFollower.tryFind(id, idFollower)).count() <= 0)
            return disappointedFind()
                    .entity("listPosts-RelationshipFollower-FORBIDDEN")
                    .build();

        query = Post
                .find("user", Sort.by("dateTime", Sort.Direction.Descending), user)
                .project(ResponsePost.class);
        final List<?> list = query.list();

        return Response.ok(list).build();
    }

}
