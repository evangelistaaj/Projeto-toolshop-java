package io.github.evangelistaaj.steps;

import io.cucumber.java.pt.Quando;
import io.github.evangelistaaj.support.Contexto;

import static io.restassured.RestAssured.given;

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
}
