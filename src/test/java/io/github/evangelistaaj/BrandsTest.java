package io.github.evangelistaaj;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BrandsTest {

    @BeforeAll
    static void configurarApi() {
        RestAssured.baseURI = "https://api.practicesoftwaretesting.com";
    }

    @Test
    void listarMarcasDevolveStatus200() {
        given()
                .when()
                .get("/brands")
                .then()
                .statusCode(200);
    }

    @Test
    void pedirMarcaInexistenteDevolveStatus404() {
        given()
                .when()
                .get("/marca-que-nao-existe")
                .then()
                .statusCode(404);
    }

}
