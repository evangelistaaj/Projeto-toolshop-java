package io.github.evangelistaaj.steps;

import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.github.evangelistaaj.support.Contexto;
import io.github.evangelistaaj.support.Oraculos;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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

    @Quando("percorro todas as páginas do catálogo, com a ordem {string}")
    public void percorroTodasAsPaginasDoCatalogoComAOrdem(String ordem) {
        String sort = sortDaOrdem(ordem);
        Response primeira = pagina(1, sort);

        contexto.totalDoCatalogo = primeira.path("total");
        int ultima = primeira.path("last_page");
        guardar(primeira);

        for (int numero = 2; numero <= ultima; numero++) {
            guardar(pagina(numero, sort));
        }
    }

    private void guardar(Response pagina) {
        contexto.idsPercorridos.addAll(pagina.jsonPath().getList("data.id", String.class));
        contexto.nomesPercorridos.addAll(pagina.jsonPath().getList("data.name", String.class));
        contexto.precosPercorridos.addAll(pagina.jsonPath().getList("data.price", Double.class));
    }

    private Response pagina(int numero, String sort) {
        RequestSpecification pedido = given().queryParam("page", numero);
        if (sort != null) {
            pedido.queryParam("sort", sort);
        }
        return pedido.when().get("/products").then().statusCode(200).extract().response();
    }

    private static String sortDaOrdem(String ordem) {
        return switch (ordem) {
            case "por omissão" -> null;
            case "nome crescente" -> "name,asc";
            case "nome decrescente" -> "name,desc";
            case "preço crescente" -> "price,asc";
            case "preço decrescente" -> "price,desc";
            default -> throw new IllegalArgumentException("Ordem desconhecida: \"" + ordem + "\"");
        };
    }

    @Então("recebo cada produto do catálogo uma só vez")
    public void receboCadaProdutoDoCatalogoUmaSoVez() {
        assertThat("produtos recebidos = total que a API indicou",
            contexto.idsPercorridos.size(), equalTo(contexto.totalDoCatalogo));
        assertThat("produtos repetidos entre páginas",
            Oraculos.repetidos(contexto.idsPercorridos), empty());
    }

    @Então("os produtos vêm ordenados por {string}")
    public void osProdutosVemOrdenadosPor(String ordem) {
        assertThat("produtos recebidos", contexto.nomesPercorridos.size(), greaterThan(1));

        List<String> paresForaDeOrdem = switch (ordem) {
            case "nome crescente" -> Oraculos.nomesForaDeOrdem(contexto.nomesPercorridos, true);
            case "nome decrescente" -> Oraculos.nomesForaDeOrdem(contexto.nomesPercorridos, false);
            case "preço crescente" -> Oraculos.precosForaDeOrdem(contexto.precosPercorridos, true);
            case "preço decrescente" -> Oraculos.precosForaDeOrdem(contexto.precosPercorridos, false);
            default -> throw new IllegalArgumentException("Ordem que não se pode verificar: \"" + ordem + "\"");
        };
        assertThat("pares fora da ordem \"" + ordem + "\"", paresForaDeOrdem, empty());
    }
}
