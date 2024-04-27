package com.br.flavioreboucassantos.quarkussocial.rest;

import com.br.flavioreboucassantos.quarkussocial.domain.model.Post;
import com.br.flavioreboucassantos.quarkussocial.domain.model.RelationshipFollower;
import com.br.flavioreboucassantos.quarkussocial.domain.model.User;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.RequestPostResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    static Long idUser;
    static Long idUserInexistent = Long.MAX_VALUE;

    static Long idFollowerNegative;
    static Long idFollowerPositive;
    static Long idFollowerInexistent = Long.MAX_VALUE;

    @BeforeAll
    @Transactional
    static public void setUP() {
        User user = new User();
        user.setName("Followed");
        user.setAge(40);
        user.persist();
        idUser = user.getId();

        Post post = new Post();
        post.setUser(user);
        post.setPostText("Hello");
        post.persist();

        User userFollowerNegative = new User();
        userFollowerNegative.setName("Follower Negative");
        userFollowerNegative.setAge(50);
        userFollowerNegative.persist();
        idFollowerNegative = userFollowerNegative.getId();

        User userFollowerPositive = new User();
        userFollowerPositive.setName("Follower Positive");
        userFollowerPositive.setAge(60);
        userFollowerPositive.persist();
        idFollowerPositive = userFollowerPositive.getId();

        RelationshipFollower relationshipFollower = new RelationshipFollower();
        relationshipFollower.setUser(user);
        relationshipFollower.setUserFollower(userFollowerPositive);
        relationshipFollower.persist();
    }

    @Test
    @DisplayName("createPost-CREATED")
    public void createPostTest() {
        RequestPostResource requestPostResource = new RequestPostResource();
        requestPostResource.setPostText("Some text");

        given()
                .pathParam("id", idUser)
                .contentType(ContentType.JSON)
                .body(requestPostResource)
                .when().post()
                .then().statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @DisplayName("createPost-id-NOT_FOUND")
    public void createPostIdNotFoundTest() {
        RequestPostResource requestPostResource = new RequestPostResource();
        requestPostResource.setPostText("Some text");

        given()
                .pathParam("id", idUserInexistent)
                .header("idFollower", idFollowerNegative)
                .contentType(ContentType.JSON)
                .body(requestPostResource)
                .when().post()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("createPost-id-NOT_FOUND"));
    }

    @Test
    @DisplayName("listPosts-id-NOT_FOUND")
    public void listPostsIdNotFoundTest() {
        given()
                .pathParam("id", idUserInexistent)
                .header("idFollower", idFollowerNegative)
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("listPosts-id-NOT_FOUND"));
    }

    @Test
    @DisplayName("listPosts-idFollower-BAD_REQUEST")
    public void listPostsIdFollowerBadRequestTest() {
        given()
                .pathParam("id", idUser)
//                .header("idFollowerNegative", -1)
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(Matchers.is("listPosts-idFollower-BAD_REQUEST"));
    }

    @Test
    @DisplayName("listPosts-idFollower-CONFLICT")
    public void listPostsIdFollowerConflictTest() {
        given()
                .pathParam("id", idUser)
                .header("idFollower", idUser)
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("listPosts-idFollower-CONFLICT"));
    }

    @Test
    @DisplayName("listPosts-idFollower-NOT_FOUND")
    public void listPostsIdFollowerNotFoundTest() {
        given()
                .pathParam("id", idUser)
                .header("idFollower", idFollowerInexistent)
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(Matchers.is("listPosts-idFollower-NOT_FOUND"));
    }

    @Test
    @DisplayName("listPosts-RelationshipFollower-FORBIDDEN")
    public void listPostsRelationshipFollowerForbiddenTest() {
        given()
                .pathParam("id", idUser)
                .header("idFollower", idFollowerNegative)
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(Response.Status.FORBIDDEN.getStatusCode())
                .body(Matchers.is("listPosts-RelationshipFollower-FORBIDDEN"));
    }

    @Test
    @DisplayName("listPosts-OK")
    public void listPostsOKTest() {
        given()
                .pathParam("id", idUser)
                .header("idFollower", idFollowerPositive)
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(Response.Status.OK.getStatusCode())
                .body("size()", Matchers.greaterThanOrEqualTo(1));
    }


}