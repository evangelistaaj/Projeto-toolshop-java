package io.github.evangelistaaj.support;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class Contexto {

    public Response resposta;
    public String idDaMarca;
    public String nomeDaMarca;
    public String slugDaMarca;
    public List<String> idsPercorridos = new ArrayList<>();
    public int totalDoCatalogo;
    public List<String> nomesPercorridos = new ArrayList<>();
    public List<Double> precosPercorridos = new ArrayList<>();
}
