package io.github.evangelistaaj.steps;

import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.github.evangelistaaj.support.Contexto;
import io.github.evangelistaaj.support.Oraculos;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Quando("consulto a página {int} do catálogo")
    public void consultoAPaginaDoCatalogo(int numero) {
        contexto.resposta = given().queryParam("page", numero).when().get("/products");
    }

    @Então("a página {int} tem {int} produtos, do {int}.º ao {int}.º")
    public void aPaginaTemProdutos(int numero, int quantidade, int primeiro, int ultimo) {
        contexto.resposta.then()
            .statusCode(200)
            .body("current_page", equalTo(numero))
            .body("per_page", equalTo(quantidade))
            .body("data", hasSize(quantidade))
            .body("from", equalTo(primeiro))
            .body("to", equalTo(ultimo));
    }

    @Quando("consulto a página a seguir à última do catálogo")
    public void consultoAPaginaASeguirAUltima() {
        int ultima = given().when().get("/products")
            .then().statusCode(200)
            .extract().path("last_page");
        contexto.resposta = given().queryParam("page", ultima + 1).when().get("/products");
    }

    @Então("a página vem vazia")
    public void aPaginaVemVazia() {
        int ultima = contexto.resposta.path("last_page");
        contexto.resposta.then()
            .statusCode(200)
            .body("current_page", equalTo(ultima + 1))
            .body("data", empty())
            .body("from", nullValue())
            .body("to", nullValue());
    }

    @Quando("consulto {string} com {string}")
    public void consultoCom(String endereco, String parametros) {
        contexto.resposta = given().when().get(endereco + "?" + parametros);
    }

    @Quando("percorro todas as páginas do catálogo")
    public void percorroTodasAsPaginasDoCatalogo() {
        Response primeira = given().queryParam("page", 1).when().get("/products")
            .then().statusCode(200)
            .extract().response();

        contexto.totalDoCatalogo = primeira.path("total");
        int ultima = primeira.path("last_page");
        contexto.idsPercorridos.addAll(primeira.path("data.id"));

        for (int pagina = 2; pagina <= ultima; pagina++) {
            List<String> ids = given().queryParam("page", pagina).when().get("/products")
                .then().statusCode(200)
                .extract().path("data.id");
            contexto.idsPercorridos.addAll(ids);
        }
    }

    @Então("recebo cada produto do catálogo uma só vez")
    public void receboCadaProdutoDoCatalogoUmaSoVez() {
        assertThat("produtos recebidos = total que a API indicou",
            contexto.idsPercorridos.size(), equalTo(contexto.totalDoCatalogo));
        assertThat("produtos repetidos entre páginas",
            Oraculos.repetidos(contexto.idsPercorridos), empty());
    }
}
