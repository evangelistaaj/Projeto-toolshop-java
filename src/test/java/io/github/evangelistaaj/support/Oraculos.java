package io.github.evangelistaaj.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Oraculos {

    public static List<String> repetidos(List<String> lista) {
        Set<String> vistos = new HashSet<>();
        List<String> repetidos = new ArrayList<>();
        for (String elemento : lista) {
            if (!vistos.add(elemento)) {
                repetidos.add(elemento);
            }
        }
        return repetidos;
    }
}
