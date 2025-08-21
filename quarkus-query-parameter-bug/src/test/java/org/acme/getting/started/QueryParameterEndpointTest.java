package org.acme.getting.started;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;


import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class QueryParameterEndpointTest {

    /**
     * For quarkus 3.15.6 this test succeeds with both quarkus-resteasy and  quarkus-rest.
     * 
     * <p>
     *
     * For quarkus 3.20.3 this test succeeds with quarkus-resteasy but fails with quarkus-rest.
     */
    @Test
    void testEndpoint() {
        given()
            .when()
            .get("/hello")
            .then()
            .statusCode(200)
            .body(containsString(QueryParameterEndpoint.IS_NULL));

        // This is the failing assertion. the "" query parameter ends up as null parameter in the endpoint.
        given()
            .when()
            .queryParam("first", "")
            .get("/hello")
            .then()
            .statusCode(200)
            .body(containsString(QueryParameterEndpoint.IS_EMPTY));
        
        given()
            .when()
            .queryParam("first", "avalue")
            .get("/hello")
            .then()
            .statusCode(200)
            .body(containsString(QueryParameterEndpoint.IS_VALID));
    }

}
