package io.github.evangelistaaj.support;

import java.util.*;

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

    public static List<String> nomesForaDeOrdem(List<String> nomes, boolean crescente) {
        List<String> pares = new ArrayList<>();
        for (int i = 1; i < nomes.size(); i++) {
            String anterior = nomes.get(i - 1);
            String seguinte = nomes.get(i);
            int comparacao = anterior.compareToIgnoreCase(seguinte);
            if (crescente ? comparacao > 0 : comparacao < 0) {
                pares.add(anterior + " antes de " + seguinte);
            }
        }
        return pares;
    }

    public static List<String> precosForaDeOrdem(List<Double> precos, boolean crescente) {
        List<String> pares = new ArrayList<>();
        for (int i = 1; i < precos.size(); i++) {
            double anterior = precos.get(i - 1);
            double seguinte = precos.get(i);
            int comparacao = Double.compare(anterior, seguinte);
            if (crescente ? comparacao > 0 : comparacao < 0) {
                pares.add(anterior + " antes de " + seguinte);
            }
        }
        return pares;
    }
}
