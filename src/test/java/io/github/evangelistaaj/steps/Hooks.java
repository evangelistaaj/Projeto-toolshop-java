package io.github.evangelistaaj.steps;

import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {

    @Before
    public void configurarApi() {
        String endereco = System.getenv().getOrDefault("TOOLSHOP_API_URL", "http://localhost:8091");
        RestAssured.baseURI = endereco;
    }
}
