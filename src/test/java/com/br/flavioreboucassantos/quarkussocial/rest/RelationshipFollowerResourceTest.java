package com.br.flavioreboucassantos.quarkussocial.rest;

import com.br.flavioreboucassantos.quarkussocial.domain.model.RelationshipFollower;
import com.br.flavioreboucassantos.quarkussocial.domain.model.User;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.RequestRelationshipFollowerResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(RelationshipFollowerResource.class)
class RelationshipFollowerResourceTest {

    static Long idUser;
    static Long idUserInexistent = Long.MAX_VALUE;

    static Long idFollowerNegativeReserved1;
    static Long idFollowerPositive;
    static Long idFollowerPositiveReserved1;
    static Long idFollowerInexistent = Long.MAX_VALUE;

    static private void persistRelationshipFollower(User user, User userFollowerPositive) {
        RelationshipFollower relationshipFollower = new RelationshipFollower();
        relationshipFollower.setUser(user);
        relationshipFollower.setUserFollower(userFollowerPositive);
        relationshipFollower.persist();
    }

    @BeforeAll
    @Transactional
    static public void setUP() {
        User user = new User();
        user.setName("Followed");
        user.setAge(40);
        user.persist();
        idUser = user.getId();

        User userFollowerNegative = new User();
        userFollowerNegative.setName("Follower Negative Reserved 1");
        userFollowerNegative.setAge(40);
        userFollowerNegative.persist();
        idFollowerNegativeReserved1 = userFollowerNegative.getId();

        User userFollowerPositive = new User();
        userFollowerPositive.setName("Follower Positive");
        userFollowerPositive.setAge(40);
        userFollowerPositive.persist();
        idFollowerPositive = userFollowerPositive.getId();

        persistRelationshipFollower(user, userFollowerPositive);

        User userFollowerPositiveReserved1 = new User();
        userFollowerPositiveReserved1.setName("Follower Positive Reserved 1");
        userFollowerPositiveReserved1.setAge(40);
        userFollowerPositiveReserved1.persist();
        idFollowerPositiveReserved1 = userFollowerPositiveReserved1.getId();

        persistRelationshipFollower(user, userFollowerPositiveReserved1);

    }

    @Test
    @DisplayName("followUser-id-NOT_FOUND")
    public void followUserIdNotFoundTest() {
        RequestRelationshipFollowerResource requestRelationshipFollowerResource = new RequestRelationshipFollowerResource();
        requestRelationshipFollowerResource.setIdFollower(idFollowerNegativeReserved1);

        given()
                .pathParam("id", idUserInexistent)
                .contentType(ContentType.JSON)
                .body(requestRelationshipFollowerResource)
                .when().put()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("followUser-id-NOT_FOUND"));
    }

    @Test
    @DisplayName("followUser-idFollower-BAD_REQUEST")
    public void followUserIdFollowerBadRequestTest() {
        RequestRelationshipFollowerResource requestRelationshipFollowerResource = new RequestRelationshipFollowerResource();
//        requestRelationshipFollowerResource.setIdFollower(-1);

        given()
                .pathParam("id", idUser)
                .contentType(ContentType.JSON)
                .body(requestRelationshipFollowerResource)
                .when().put()
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(Matchers.is("followUser-idFollower-BAD_REQUEST"));
    }

    @Test
    @DisplayName("followUser-idFollower-CONFLICT")
    public void followUserIdFollowerConflictTest() {
        RequestRelationshipFollowerResource requestRelationshipFollowerResource = new RequestRelationshipFollowerResource();
        requestRelationshipFollowerResource.setIdFollower(idUser);

        given()
                .pathParam("id", idUser)
                .contentType(ContentType.JSON)
                .body(requestRelationshipFollowerResource)
                .when().put()
                .then().statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("followUser-idFollower-CONFLICT"));
    }

    @Test
    @DisplayName("followUser-idFollower-NOT_FOUND")
    public void followUserIdFollowerNotFoundTest() {
        RequestRelationshipFollowerResource requestRelationshipFollowerResource = new RequestRelationshipFollowerResource();
        requestRelationshipFollowerResource.setIdFollower(idFollowerInexistent);

        given()
                .pathParam("id", idUser)
                .contentType(ContentType.JSON)
                .body(requestRelationshipFollowerResource)
                .when().put()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("followUser-idFollower-NOT_FOUND"));
    }

    @Test
    @DisplayName("followUser-RelationshipFollower-NOT_MODIFIED")
    public void followUserRelationshipFollowerNotModifiedTest() {
        RequestRelationshipFollowerResource requestRelationshipFollowerResource = new RequestRelationshipFollowerResource();
        requestRelationshipFollowerResource.setIdFollower(idFollowerPositive);

        given()
                .pathParam("id", idUser)
                .contentType(ContentType.JSON)
                .body(requestRelationshipFollowerResource)
                .when().put()
                .then().statusCode(Response.Status.NOT_MODIFIED.getStatusCode());
    }

    @Test
    @DisplayName("followUser-NO_CONTENT")
    public void followUserNoContentTest() {
        RequestRelationshipFollowerResource requestRelationshipFollowerResource = new RequestRelationshipFollowerResource();
        requestRelationshipFollowerResource.setIdFollower(idFollowerNegativeReserved1);

        given()
                .pathParam("id", idUser)
                .contentType(ContentType.JSON)
                .body(requestRelationshipFollowerResource)
                .when().put()
                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("listFollowers-id-NOT_FOUND")
    public void listFollowersIdNotFoundTest() {
        given()
                .pathParam("id", idUserInexistent)
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("listFollowers-id-NOT_FOUND"));
    }

    @Test
    @DisplayName("listFollowers-OK")
    public void listFollowersTest() {
        io.restassured.response.Response response = given()
                .pathParam("id", idUser)
                .contentType(ContentType.JSON)
                .when().get()
                .then().extract().response();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        int count = response.jsonPath().getInt("count");
        assertTrue(count >= 1);

        List<Object> list = response.jsonPath().getList("list");
        assertEquals(count, list.size());
    }

    @Test
    @DisplayName("unfollowUser-id-NOT_FOUND")
    public void unfollowUserIdNotFoundTest() {
        given()
                .pathParam("id", idUserInexistent)
                .queryParam("idFollower", idFollowerNegativeReserved1)
                .contentType(ContentType.JSON)
                .when().delete()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("unfollowUser-id-NOT_FOUND"));
    }

    @Test
    @DisplayName("unfollowUser-idFollower-BAD_REQUEST")
    public void unfollowUserIdFollowerBadRequestTest() {
        given()
                .pathParam("id", idUser)
//                .queryParam("idFollower", -1)
                .contentType(ContentType.JSON)
                .when().delete()
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(Matchers.is("unfollowUser-idFollower-BAD_REQUEST"));
    }

    @Test
    @DisplayName("unfollowUser-idFollower-CONFLICT")
    public void unfollowUserIdFollowerConflictTest() {
        given()
                .pathParam("id", idUser)
                .queryParam("idFollower", idUser)
                .contentType(ContentType.JSON)
                .when().delete()
                .then().statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("unfollowUser-idFollower-CONFLICT"));
    }

    @Test
    @DisplayName("unfollowUser-idFollower-NOT_FOUND")
    public void unfollowUserIdFollowerNotFoundTest() {
        given()
                .pathParam("id", idUser)
                .queryParam("idFollower", idFollowerInexistent)
                .contentType(ContentType.JSON)
                .when().delete()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("unfollowUser-idFollower-NOT_FOUND"));
    }

    @Test
    @DisplayName("unfollowUser-NO_CONTENT")
    public void unfollowUserNoContentTest() {
        given()
                .pathParam("id", idUser)
                .queryParam("idFollower", idFollowerPositiveReserved1)
                .contentType(ContentType.JSON)
                .when().delete()
                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}