# 🧠 Cábula: como pensar ao escrever um passo (Quando / Então)

Cada passo responde a **uma pergunta**. O esqueleto é sempre o mesmo; o recheio muda conforme o cenário.

| Passo | A pergunta que responde | Começa sempre por... | Acaba sempre por... |
|---|---|---|---|
| **`Quando`** | "**O que é que eu faço?**" (a ação: o pedido à API) | `given()...` | **pousar a resposta no balcão:** `contexto.resposta = ...` |
| **`Então`** | "**Como é que eu sei que correu bem?**" (a prova) | **ir buscar a resposta ao balcão:** `contexto.resposta.then()` | uma ou mais verificações: `.statusCode(...)`, `.body(...)` |

🍽️ **Em restaurante:** o `Quando` é o cozinheiro (prepara o prato e pousa-o no balcão; às vezes vai primeiro à despensa buscar um ingrediente). O `Então` é o empregado (pega no prato e confere-o antes de servir; se o cozinheiro deixou um bilhete, confere o prato contra o bilhete).

---

## 🎬 No `Quando`: "o que preciso para fazer o pedido?"

| Se precisas de... | Receita | Exemplo |
|---|---|---|
| um pedido **simples** | `contexto.resposta = given().when().get("/brands");` | a lista de marcas |
| um **id que vem do cenário** | `@Quando("peço a marca com o id {string}")` + `get("/brands/" + id)` | a marca que não existe |
| **parâmetros** (`?page=2`) | `given().queryParam("page", numero).when().get("/products")` | a página 2 do catálogo |
| endereço e parâmetros **escritos no cenário** | `get(endereco + "?" + parametros)` | a página inválida (bug #28) |
| algo que **não sabes de antemão** (um id, a última página) | 1) pedir primeiro; 2) **tirar** o valor com `.extract().path("...")`; 3) só depois fazer o pedido principal | a página a seguir à última; a marca que existe |
| que o `Então` **compare** com algo que escolheste | **guardar** num bolso do `Contexto`: `contexto.idDaMarca = ...` | a marca que existe |

```java
// Exemplo completo: "descobrir antes" + "guardar para o Então"
@Quando("peço uma marca da lista pelo seu id")
public void pecoUmaMarcaDaListaPeloSeuId() {
    Response lista = given().when().get("/brands")          // 1) pedir primeiro
            .then().statusCode(200)
            .extract().response();
    contexto.idDaMarca = lista.path("id[0]");                // 2) tirar e guardar
    contexto.resposta = given().when().get("/brands/" + contexto.idDaMarca);  // 3) o pedido principal
}
```

⚠️ **Cuidados que já nos apanharam:**
- A barra no caminho: `"/brands/" + id`, e não `"/brands" + id`.
- O texto da anotação tem de ser **igual** à frase do Gherkin (se a frase não tem aspas, a anotação não tem `{string}`).
- Nunca escrever à mão valores que mudam de ambiente (ids, totais, a última página): ir buscá-los à própria resposta.

---

## ✅ No `Então`: "o que prova que correu bem?"

| Se queres provar que... | Receita | Exemplo |
|---|---|---|
| o **código** está certo | `.statusCode(200)` | quase todos |
| um **campo** tem um valor | `.body("current_page", equalTo(2))` | a página 2 |
| uma **lista** não está vazia | `.body("data", not(empty()))` | o catálogo |
| uma lista tem **N elementos** | `.body("data", hasSize(9))` | a página 2 |
| **todos** os elementos cumprem algo | `.body("data.price", everyItem(instanceOf(Number.class)))` | o catálogo |
| algo vem **vazio de propósito** | `.body("from", nullValue())` | a página vazia |
| veio **aquilo que o `Quando` escolheu** | `.body("id", equalTo(contexto.idDaMarca))` | a marca que existe |

⚠️ **Cuidados:**
- A **ordem** das verificações importa: o REST Assured para na primeira que falha. Põe primeiro a que dá a mensagem mais útil.
- Um `Então` que só verifica o `200` prova pouco: pergunta-te "uma API com bug também passava aqui?".
- O **esperado** vem sempre de **fora** do sistema (Swagger, consistência, normas), e nunca da própria resposta.

---

## 📋 Quando há muitos valores: listas, ciclos e conjuntos

| Preciso de... | Receita | Analogia |
|---|---|---|
| guardar **muitos** valores | `List<String> ids = new ArrayList<>();` · `ids.add(x)` · `ids.addAll(outra)` · `ids.size()` · `ids.get(0)` (o 1.º) | 📒 um caderno |
| **repetir** um pedido para cada página | `for (int pagina = 2; pagina <= ultima; pagina++) { ... }` | 🔁 "para a página 2, 3, ... até à última" |
| percorrer **cada elemento** de uma lista | `for (String id : ids) { ... }` | "para cada id do caderno" |
| saber se algo **já apareceu** | `Set<String> vistos = new HashSet<>();` · `vistos.add(x)` devolve `false` se já lá estava | 🚪 um porteiro com memória |
| verificar um valor **meu** (e não da resposta) | `assertThat("mensagem se falhar", oQueTenho, equalTo(oQueDevia))` | |

⚠️ Tudo o que está **entre as chavetas `{ }`** de um `for` repete-se a cada volta; o que está **depois** do `}` corre uma vez.

🧠 **Uma verificação sozinha pode ser enganada.** "Recebi 200 produtos" passa mesmo que um venha repetido e outro falte (+1 −1). Juntar "nenhum repetido" fecha o buraco: 200 ids todos diferentes num catálogo de 200 = não falta nenhum.

🔮 **Funções de oráculo** (verificações que vamos reutilizar) vivem no `support/Oraculos.java`, como o `support/oraculos.ts` do projeto 2: `Oraculos.repetidos(lista)` devolve a lista dos repetidos (vazia = tudo bem).

---

## 🔬 Testar uma função de oráculo (testes unitários)

```java
class OraculosTest {                                   // o nome acaba em Test → o mvnw test corre-o
    @Test
    @DisplayName("Um elemento repetido é encontrado")  // o nome legível, em português
    void umElementoRepetido() {
        assertThat(Oraculos.repetidos(List.of("A", "B", "A")), contains("A"));
    }
}
```
- `List.of("A", "B")` cria uma lista já com valores (dados inventados).
- `contains("A")` = "tem **exatamente** isto"; `hasItem("A")` = "tem isto, e talvez mais".
- ⚖️ Sempre **dois tipos**: um **caso que tem de apanhar** (há repetidos → encontra-os) e um **controlo que tem de deixar passar** (não há → não inventa). Uma balança que diz sempre "zero" passa nos controlos; só os casos a apanham.

---

## 🗺️ Caminhos dentro do JSON (o 1.º argumento do `.body(...)` e do `.path(...)`)

| Caminho | Dá... |
|---|---|
| `"total"` | o valor de um campo |
| `"brand.name"` | um campo **dentro** de outro ("caixas dentro de caixas") |
| `"data.name"` | o `name` de **todos** os elementos da lista `data` |
| `"id[0]"` | o `id` do **primeiro** elemento (as listas começam no **0**) |

---

## 📍 Comum ou da funcionalidade?

> "Esta frase faria sentido noutro `.feature`?"

- Só fala da resposta HTTP (código, contrato) → **`ComumSteps`**.
- Usa palavras do negócio (página, produto, marca) → a classe **da funcionalidade** (`MarcasSteps`, `CatalogoSteps`…).

## 📦 Imports que aparecem sempre

```java
import static io.restassured.RestAssured.given;   // given()
import static org.hamcrest.Matchers.*;            // equalTo, hasSize, empty, everyItem, nullValue...
import static org.hamcrest.MatcherAssert.assertThat; // verificar valores que não vêm da resposta
import java.util.List; import java.util.ArrayList; // listas
import java.util.Set; import java.util.HashSet;     // conjuntos
import io.restassured.response.Response;          // guardar uma resposta numa variável
import io.cucumber.java.pt.Quando;                // @Quando
import io.cucumber.java.pt.Então;                 // @Então
```
Truques: `Alt+Enter` numa palavra vermelha (importa) · `Ctrl+Alt+O` (apaga imports sem uso).
