package io.github.evangelistaaj.steps;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class MarcasSteps {

    private Response resposta;

    @Before
    public void configurarApi() {
        RestAssured.baseURI = "https://api.practicesoftwaretesting.com";
    }

    @Quando("peço a lista de marcas")
    public void pecoAListaDeMarcas() {
        resposta = given().when().get("/brands");
    }

    @Quando("peço a marca com o id {string}")
    public void pecoAMarcaComOId(String id) {
        resposta = given().when().get("/brands/" + id);
    }

    @Então("a resposta tem o status {int}")
    public void aRespostaTemOStatus(int statusEsperado) {
        resposta.then().statusCode(statusEsperado);
    }
}