package io.github.evangelistaaj.steps;

import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.github.evangelistaaj.support.Contexto;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MarcasSteps {

    private final Contexto contexto;

    public MarcasSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    @Quando("peço a lista de marcas")
    public void pecoAListaDeMarcas() {
        contexto.resposta = given().when().get("/brands");
    }

    @Quando("peço a marca com o id {string}")
    public void pecoAMarcaComOId(String id) {
        contexto.resposta = given().when().get("/brands/" + id);
    }

    @Quando("peço uma marca da lista pelo seu id")
    public void pecoUmaMarcaDaListaPeloSeuId() {
        Response lista = given().when().get("/brands")
            .then().statusCode(200)
            .extract().response();

        contexto.idDaMarca = lista.path("id[0]");
        contexto.nomeDaMarca = lista.path("name[0]");
        contexto.slugDaMarca = lista.path("slug[0]");

        contexto.resposta = given().when().get("/brands/" + contexto.idDaMarca);
    }

    @Então("recebo essa marca, com o mesmo id, nome e slug")
    public void receboEssaMarca() {
        contexto.resposta.then()
            .statusCode(200)
            .body("id", equalTo(contexto.idDaMarca))
            .body("name", equalTo(contexto.nomeDaMarca))
            .body("slug", equalTo(contexto.slugDaMarca));
    }
}
