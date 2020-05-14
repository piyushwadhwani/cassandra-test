package com.tekdynamix.cassandra;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.CassandraContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
//@QuarkusTestResource(CassandraDBResource.class)
public class CassandraResourceTest {
    private static final Logger log = LoggerFactory.getLogger(CassandraDBResource.class);

    @Test
    public void testHelloEndpoint() {
        log.info("Starting Testing for Rest Endpoing test");
        given()
                .when().get("/test")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

}