package io.github.evangelistaaj.steps;

import io.cucumber.java.pt.Então;
import io.github.evangelistaaj.support.Contexto;

public class ComumSteps {

    private final Contexto contexto;

    public ComumSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    @Então("recebo uma resposta com o código {int}")
    public void receboUmaRespostaComOCodigo(int codigoEsperado) {
        contexto.resposta.then().statusCode(codigoEsperado);
    }
}
