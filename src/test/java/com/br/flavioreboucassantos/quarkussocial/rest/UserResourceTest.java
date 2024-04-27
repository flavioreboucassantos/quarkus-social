package com.br.flavioreboucassantos.quarkussocial.rest;

import com.br.flavioreboucassantos.quarkussocial.rest.dto.ErrorResponse;
import com.br.flavioreboucassantos.quarkussocial.rest.dto.RequestUserResource;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @TestHTTPResource("/users")
    URL resourceURL;

    @Test
    @DisplayName("createUser-CREATED")
    @Order(0)
    public void createUserTest() {
        RequestUserResource requestUserResource = new RequestUserResource();
        requestUserResource.setName("Fulano");
        requestUserResource.setAge(40);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestUserResource)
                .when().post(resourceURL)
                .then().extract().response();

        assertEquals(jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode(), response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("createUser-requestUserResource-UNPROCESSABLE_ENTITY_STATUS")
    public void createUserValidationErrorTest() {
        RequestUserResource requestUserResource = new RequestUserResource();
        requestUserResource.setName(null);
        requestUserResource.setAge(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestUserResource)
                .when().post(resourceURL)
                .then().extract().response();

        assertEquals(ErrorResponse.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
//        assertEquals("Age is Required", errors.get(0).get("message"));
//        assertEquals("Name is Required", errors.get(1).get("message"));
    }

    @Test
    @DisplayName("listAll-OK")
    public void listAllUsersTest() {
        given()
                .contentType(ContentType.JSON)
                .when().get(resourceURL)
                .then()
                .statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
                .body("size()", Matchers.greaterThanOrEqualTo(1));
    }

}