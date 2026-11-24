package io.github.evangelistaaj.steps;

import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.github.evangelistaaj.support.Contexto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CatalogoSteps {

    private final Contexto contexto;

    public CatalogoSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    @Quando("consulto o catálogo de produtos")
    public void consultoOCatalogoDeProdutos() {
        contexto.resposta = given().when().get("/products");
    }

    @Então("recebo uma lista de produtos")
    public void receboUmaListaDeProdutos() {
        contexto.resposta.then()
            .statusCode(200)
            .body("data", not(empty()))
            .body("data.id", everyItem(instanceOf(String.class)))
            .body("data.name", everyItem(instanceOf(String.class)))
            .body("data.price", everyItem(instanceOf(Number.class)))
            .body("data.in_stock", everyItem(instanceOf(Boolean.class)));
    }
}
