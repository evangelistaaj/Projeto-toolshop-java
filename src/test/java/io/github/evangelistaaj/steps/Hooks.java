package io.github.evangelistaaj.steps;

import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {

    @Before
    public void configurarApi() {
        RestAssured.baseURI = "https://api.practicesoftwaretesting.com";
    }
}
