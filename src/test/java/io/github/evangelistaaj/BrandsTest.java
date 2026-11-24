package io.github.evangelistaaj;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BrandsTest {

    @Test
    void listarMarcasDevolveStatus200() {
        given()
                .when()
                .get("https://api.practicesoftwaretesting.com/brands")
                .then()
                .statusCode(200);
    }

    @Test
    void pedirMarcaInexistenteDevolveStatus404() {
        given()
                .when()
                .get("https://api.practicesoftwaretesting.com/brands/marca-que-nao-existe")
                .then()
                .statusCode(404);
    }

}
