package com.br.flavioreboucassantos.quarkussocial.rest;

import com.br.flavioreboucassantos.quarkussocial.domain.model.RelationshipFollower;
import com.br.flavioreboucassantos.quarkussocial.domain.model.User;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.RequestRelationshipFollowerResource;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.ResponseRelationshipFollowerResourceList;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.ResponseRelationshipFollowerResourceProjectListItem2;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users/{id}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RelationshipFollowerResource extends BaseResource {

    @PUT
    @Transactional
    public Response followUser(@PathParam("id") Long id, RequestRelationshipFollowerResource requestRelationshipFollowerResource) {
        User user;
        if ((user = User.tryFindById(id)) == null)
            return disappointedFindById()
                    .entity("followUser-id-NOT_FOUND")
                    .build();

        Long idFollower = requestRelationshipFollowerResource.getIdFollower();
        if (tryIdBadRequest(idFollower))
            return disappointedIdBadRequest()
                    .entity("followUser-idFollower-BAD_REQUEST")
                    .build();

        if (tryIdConflict(id, idFollower))
            return disappointedIdConflict()
                    .entity("followUser-idFollower-CONFLICT")
                    .build();

        User userFollower;
        if ((userFollower = User.tryFindById(idFollower)) == null)
            return disappointedFindById()
                    .entity("followUser-idFollower-NOT_FOUND")
                    .build();

        RelationshipFollower relationshipFollower = new RelationshipFollower();
        relationshipFollower.setUser(user);
        relationshipFollower.setUserFollower(userFollower);

        Long idRelationshipFollower;
        if ((idRelationshipFollower = relationshipFollower.tryPersist()) == null)
            return disappointedPersist()
                    .build();

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("id") Long id) {
        User user;
        if ((user = User.tryFindById(id)) == null)
            return disappointedFindById()
                    .entity("listFollowers-id-NOT_FOUND")
                    .build();

//        List<ResponseRelationshipFollowerResourceProjectListItem> followers = RelationshipFollower.findByFollowed(id);
        List<ResponseRelationshipFollowerResourceProjectListItem2> followers = RelationshipFollower.findByFollowed2(id);

        ResponseRelationshipFollowerResourceList responseRelationshipFollowerResourceList = new ResponseRelationshipFollowerResourceList();
        responseRelationshipFollowerResourceList.setCount(followers.size());
        responseRelationshipFollowerResourceList.setList(followers);

        return Response.ok().entity(responseRelationshipFollowerResourceList).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("id") Long id, @QueryParam("idFollower") Long idFollower) {
        User user;
        if ((user = User.tryFindById(id)) == null)
            return disappointedFindById()
                    .entity("unfollowUser-id-NOT_FOUND")
                    .build();

        if (tryIdBadRequest(idFollower))
            return disappointedIdBadRequest()
                    .entity("unfollowUser-idFollower-BAD_REQUEST")
                    .build();

        if (tryIdConflict(id, idFollower))
            return disappointedIdConflict()
                    .entity("unfollowUser-idFollower-CONFLICT")
                    .build();

        User userFollower;
        if ((userFollower = User.tryFindById(idFollower)) == null)
            return disappointedFindById()
                    .entity("unfollowUser-idFollower-NOT_FOUND")
                    .build();

        RelationshipFollower.deleteByUserAndFollower(id, idFollower);

        return Response.status(Response.Status.NO_CONTENT).build();
    }


}
