package io.github.evangelistaaj.unitarios;

import io.github.evangelistaaj.support.Oraculos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class OraculosTest {

    @Test
    @DisplayName("Uma lista sem repetidos devolve uma lista vazia")
    void listaSemRepetidos() {
        assertThat(Oraculos.repetidos(List.of("A", "B", "C")), empty());
    }

    @Test
    @DisplayName("Uma lista vazia não tem repetidos")
    void listaVazia() {
        assertThat(Oraculos.repetidos(List.of()), empty());
    }

    @Test
    @DisplayName("Um elemento repetido é encontrado")
    void umElementoRepetido() {
        assertThat(Oraculos.repetidos(List.of("A", "B", "A")), contains("A"));
    }

    @Test
    @DisplayName("Um repetido longe do original também é encontrado")
    void repetidoAfastado() {
        assertThat(Oraculos.repetidos(List.of("A", "B", "C", "D", "B")), contains("B"));
    }
}
