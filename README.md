# 🧰 Projeto Toolshop — API em Java + REST Assured

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://adoptium.net/)
[![REST Assured](https://img.shields.io/badge/REST%20Assured-6.3.0-green)](https://rest-assured.io/)
[![Cucumber](https://img.shields.io/badge/Cucumber--JVM-8.0.2-brightgreen?logo=cucumber)](https://cucumber.io/)
[![JUnit](https://img.shields.io/badge/JUnit-6.1.3-red?logo=junit5)](https://junit.org/)
[![Licença: MIT](https://img.shields.io/badge/licen%C3%A7a-MIT-blue)](LICENSE)

> Testes automatizados da API da [Toolshop](https://practicesoftwaretesting.com) em **Java**, com **REST Assured**, **Cucumber-JVM** e **JUnit**. Os cenários estão escritos em **português** (Gherkin).

---

## 📖 Sobre

Este é o **terceiro projeto** do meu portfolio de automação de testes. A aplicação é a mesma dos anteriores, mas a stack muda:

| Projeto | O que testa | Stack |
|---|---|---|
| [Projeto-toolshop](https://github.com/evangelistaaj/Projeto-toolshop) | Interface (E2E) | Playwright + playwright-bdd |
| [Projeto-toolshop-api](https://github.com/evangelistaaj/Projeto-toolshop-api) | API REST | Playwright (`APIRequestContext`) + Cucumber |
| **Projeto-toolshop-java** (este) | API REST | Java + REST Assured + Cucumber-JVM + JUnit |

**Porquê o mesmo sistema outra vez?** Porque eu já conheço o domínio de trás para a frente: marcas, produtos, utilizadores, login… Assim, toda a energia vai para aprender a stack que o mercado mais pede em Portugal para *Automation Tester*: **Java + REST Assured + Cucumber**.

E, desta vez, **o código é escrito por mim, linha a linha**. 💪

---

## 🛠️ Tecnologias

| Ferramenta | Versão | Para quê |
|---|---|---|
| ☕ Java (Eclipse Temurin) | 21 (LTS) | linguagem |
| 📦 Maven | via Maven Wrapper (`mvnw`) | dependências, build e execução no terminal |
| 🌐 REST Assured | 6.3.0 | pedidos HTTP e verificações (`given / when / then`) |
| 🥒 Cucumber-JVM | 8.0.2 | cenários em Gherkin, em português |
| ✅ JUnit Platform | 6.1.3 | motor de execução dos testes (e os dois perfis: normal e bugs) |
| 🎒 PicoContainer | via Cucumber 8.0.2 | partilhar o `Contexto` entre as classes de passos, um novo por cenário |
| 🎯 Hamcrest | via REST Assured | verificações legíveis (`equalTo`, `hasSize`, `everyItem`…) |
| 💡 IntelliJ IDEA | 2026.3 | editor (com o plugin *Cucumber for Java*) |

---

## ▶️ Como executar

**Pré-requisitos:**
- IntelliJ IDEA com o plugin **Cucumber for Java**. O próprio IntelliJ descarrega o JDK 21 e já traz o Maven.
- A Toolshop a correr localmente com Docker ([instruções no repositório original](https://github.com/testsmith-io/practice-software-testing)), com a API em `http://localhost:8091`.

1. Clonar o repositório:
   ```bash
   git clone https://github.com/evangelistaaj/Projeto-toolshop-java.git
   ```
2. Abrir a pasta no IntelliJ e deixar o Maven descarregar as dependências.
3. Correr os cenários, de uma destas formas:
   - **No IntelliJ, todos:** ▶️ ao lado de `ExecutarCucumberTest` (o runner).
   - **No IntelliJ, uma funcionalidade ou um cenário:** ▶️ dentro do ficheiro `.feature`.
   - **No terminal** (sem IntelliJ, como na CI), com o Maven Wrapper, que descarrega a versão certa do Maven na primeira vez:
     ```powershell
     .\mvnw.cmd test     # Windows
     ./mvnw test          # Linux e macOS
     ```

### 🐞 Dois perfis: normal e bugs conhecidos

Os cenários de bugs já confirmados têm a tag `@bug-conhecido` e falham **de propósito**: o esperado é o comportamento certo, e não o que a API faz hoje.

| Perfil | O que corre | Comando | Resultado esperado |
|---|---|---|---|
| 🟢 Normal | `ExecutarCucumberTest` (`@ExcludeTags("bug-conhecido")`) e os testes unitários | `.\mvnw.cmd test` | tudo verde |
| 🐞 Bugs conhecidos | `ExecutarBugsConhecidos` (`@IncludeTags("bug-conhecido")`) | `.\mvnw.cmd test -Dtest=ExecutarBugsConhecidos` | falham enquanto o bug existir |

O runner dos bugs não acaba em `Test`, por isso o `mvnw test` normal não o corre. Se um dia a Toolshop corrigir um bug, o cenário fica verde no perfil dos bugs, e é sinal para tirar a tag.

### 🐳 Onde correm os testes

Por omissão, os testes correm contra a **Toolshop local** (`http://localhost:8091`). Para usar outro endereço, sem mexer no código, define a variável de ambiente `TOOLSHOP_API_URL`:

```powershell
$env:TOOLSHOP_API_URL = "https://api.practicesoftwaretesting.com"
```

> ⚠️ **Porquê o local por omissão?** A API pública é partilhada por muita gente. Durante a exploração, o catálogo público passou de **50 para 141 e depois para 150 produtos em poucos minutos**, porque outras pessoas estavam a criar produtos. Na Toolshop local, os totais mantêm-se estáveis, e os oráculos que comparam com o catálogo completo podem confiar no que percorrem. No ambiente público, só leituras (GET).

---

## 🥒 Exemplo de cenário

```gherkin
# language: pt
Funcionalidade: Marcas
  Como cliente da Toolshop
  Quero consultar as marcas disponíveis
  Para encontrar os produtos da marca que prefiro

  Cenário: Listar todas as marcas
    Quando peço a lista de marcas
    Então recebo uma resposta com o código 200

  Regra: Cada marca consulta-se pelo seu id
  Pedir uma marca que existe devolve essa marca. Pedir uma marca
  que não existe devolve "não encontrado" (404).

    Cenário: Pedir uma marca que existe
      Quando peço uma marca da lista pelo seu id
      Então recebo essa marca, com o mesmo id, nome e slug

    Cenário: Pedir uma marca que não existe
      Quando peço a marca com o id "marca-que-nao-existe"
      Então recebo uma resposta com o código 404
```

E o "dicionário" que liga o português ao Java. O `Quando` escolhe uma marca da lista (os ids mudam de ambiente para ambiente) e deixa-a no `Contexto`, partilhado entre as classes de passos; o `Então` confirma que veio **essa** marca:

```java
@Quando("peço uma marca da lista pelo seu id")
public void pecoUmaMarcaDaListaPeloSeuId() {
    Response lista = given().when().get("/brands")
            .then().statusCode(200)
            .extract().response();

    contexto.idDaMarca = lista.path("id[0]");
    contexto.nomeDaMarca = lista.path("name[0]");
    contexto.slugDaMarca = lista.path("slug[0]");

    contexto.resposta = given().when().get("/brands/" + contexto.idDaMarca);
}

@Então("recebo essa marca, com o mesmo id, nome e slug")
public void receboEssaMarca() {
    contexto.resposta.then()
            .statusCode(200)
            .body("id", equalTo(contexto.idDaMarca))
            .body("name", equalTo(contexto.nomeDaMarca))
            .body("slug", equalTo(contexto.slugDaMarca));
}
```

---

## 📊 Estado atual

| Funcionalidade | Cenários | Estado |
|---|---|---|
| 🏷️ Marcas | 3 | ✅ a passar |
| 🧰 Catálogo | 13 + 4 🐞 | ✅ a passar; 4 exemplos confirmam o bug [#28](https://github.com/evangelistaaj/Projeto-toolshop-api/issues/28) |

🚧 Projeto em construção: os cenários do [projeto 2](https://github.com/evangelistaaj/Projeto-toolshop-api) vão chegar aqui, funcionalidade a funcionalidade.

---

## 🔍 O que é verificado

- **Marcas**
  - ✅ A lista de marcas responde com status **200**.
  - ✅ Pedir uma marca que existe devolve **essa** marca (mesmo `id`, `name` e `slug`). A marca é escolhida da própria lista, porque os ids mudam de ambiente para ambiente (a mesma marca tem ids diferentes no público e no Docker).
  - ✅ Pedir uma marca que não existe responde com **404** (caso negativo).
- **Catálogo**
  - ✅ A lista de produtos não vem vazia, e cada produto tem `id` e `name` (texto), `price` (número) e `in_stock` (verdadeiro/falso).
  - ✅ A página 2 tem 9 produtos, do 10.º ao 18.º (página, produtos por página, tamanho da lista e posição).
  - ✅ Percorrer todas as páginas, **por omissão, por nome e por preço**, devolve **cada produto uma só vez** (com preços iguais, uma ordenação sem desempate estável podia repetir ou perder produtos entre páginas): o número de produtos recebidos é o `total` que a API indica, e nenhum se repete (`Oraculos.repetidos`). As duas verificações juntas apanham o caso "+1 −1" (um produto repetido a esconder um desaparecido), que a contagem sozinha deixaria passar.
  - ✅ A **ordenação vale para o catálogo inteiro** (nome e preço, crescente e decrescente): percorridas todas as páginas, nenhum par de vizinhos está fora de ordem (`Oraculos.nomesForaDeOrdem` e `precosForaDeOrdem`). Valores iguais podem vir em qualquer ordem; os nomes comparam-se sem distinguir maiúsculas.
  - ✅ A página a seguir à última vem vazia, sem posição (`from` e `to` a `null`). A última página é lida da própria resposta, porque muda de ambiente para ambiente.
  - 🐞 Uma página inválida (**valores-limite** `0` e `-1`; **não inteiros** `abc` e `1.5`) devia ser recusada com **422**. Hoje a API devolve **200 com a página 1**, em silêncio.

---

## 🧠 Boas práticas e decisões

- **Três provas para cada verificação nova:**
  - **OK**: o caso feliz passa.
  - **NOK**: o caso negativo comporta-se como deve.
  - **Sabotagem**: altero o código de propósito para o teste ficar vermelho, e depois desfaço. Se a sabotagem não for apanhada, o teste não está a verificar nada.
- **Desconfiar dos verdes:** um teste pode passar pela razão errada (*falso positivo*). Exemplo real deste projeto: um endereço mal escrito também devolvia 404, por isso o teste "da marca inexistente" passava sem testar marca nenhuma. A sabotagem e a revisão do código apanharam-no.
- **O esperado vem sempre de fora do sistema:** do Swagger, da consistência com o resto da API e das normas HTTP, e nunca da própria resposta (um teste que concorda sempre com o sistema não testa nada). Quando a fonte é fraca, a descrição da Regra di-lo.
- **Testar os testes:** as funções de oráculo (`support/Oraculos.java`) têm testes unitários com dados inventados (`unitarios/OraculosTest.java`), que correm em milissegundos no `mvnw test`. Cada função é posta à prova com **um caso que tem de apanhar** e **um controlo que tem de deixar passar**: com a sabotagem "devolve sempre vazio", os controlos continuavam verdes e só os casos a apanharam.
- **Verificações que se complementam:** no catálogo completo, "recebi o total" e "nenhum repetido" juntas apanham o que cada uma sozinha deixaria passar.
- **Explorar antes de automatizar:** cada funcionalidade começa no Swagger (local) ou no terminal, só com leituras, e só depois passa a cenário.
- **Gherkin declarativo, em português:** um só `Quando` por cenário, `Regra:` com descrição e passos reutilizáveis com parâmetros (`{string}`, `{int}`).
- **Endereço da API num só sítio:** um *hook* `@Before` do Cucumber define o `baseURI`, e os passos usam só o caminho (`/brands`).
- **Versões geridas por BOM:** o JUnit e o Cucumber usam BOMs (*Bill of Materials*) no `dependencyManagement`, para as versões de cada família de bibliotecas serem compatíveis entre si.
- **Finais de linha consistentes:** o `.editorconfig` faz o editor escrever com LF, e o `.gitattributes` faz o Git guardar com LF, em qualquer sistema operativo.

---

## 🐞 Bugs encontrados

Os bugs só passam a *issue* depois de um teste os confirmar. Este projeto testa a mesma Toolshop do [projeto 2](https://github.com/evangelistaaj/Projeto-toolshop-api), por isso os bugs já conhecidos apontam para os issues de lá.

| Issue | Bug | Confirmado aqui |
|---|---|---|
| [#28](https://github.com/evangelistaaj/Projeto-toolshop-api/issues/28) | Os parâmetros do catálogo não são validados: um pedido inválido é aceite com 200 | ✅ caso novo: o `page` (`0`, `-1`, `abc`, `1.5`) devolve a página 1, em vez de 422 |

---

## 💡 Descobertas

- 🪚 **"Wood Saw" antes de "Wooden Workbench": o erro estava no teste.** A primeira versão do oráculo da ordenação usava o `Collator` do Java (regras do inglês), que **ignora os espaços**: comparava "woodsaw" com "woodenworkbench" e acusava a API. A API estava certa: como num dicionário, o espaço vem antes das letras. Lição: traduzir uma ferramenta (o `localeCompare` do projeto 2) não é traduzir o comportamento. O oráculo passou a usar `compareToIgnoreCase`, que serve para estes nomes (em inglês, sem acentos); o plano é validar um dia contra a própria base de dados (SQL).
- 🧪 **Produtos de teste na base local (hipótese):** a Toolshop local tem dezenas de produtos como `Wood Chisel Set 4 Pieces - ref. MWP6U00AA0L`, que não existem nos dados iniciais. Parecem ter ficado de execuções do projeto 2; falta confirmar. Não afeta os testes deste projeto, que leem o total da própria resposta.
- 🗄️ **Cache das respostas (explorado na Toolshop local):** os dados públicos (`/brands`, `/categories`, `/products`) vêm com `Cache-Control: max-age=120, public` (podem ser guardados 2 minutos, por qualquer cache). Os dados pessoais (`/users/me`, `/invoices`) vêm com `no-cache, private`: nenhuma cache partilhada os guarda. A hipótese de risco **não se confirmou**. Nota de *hardening*: para dados sensíveis, a OWASP recomenda `no-store` (nem o browser guarda); com `no-cache, private`, uma fatura pode ficar no disco do browser, e as faturas chegaram a expor o número do cartão e o CVV ([#14 do projeto 2](https://github.com/evangelistaaj/Projeto-toolshop-api/issues/14)). A cache de 120 s também **não explica** o atraso do stock ([#10](https://github.com/evangelistaaj/Projeto-toolshop-api/issues/10)): o atraso chega aos 5 minutos e acontece sem nenhuma cache pelo meio.
- 📄 **O 422 está no Swagger, mas não no `GET /products`:** a documentação define o 422 (`UnprocessableEntityResponse`, "the server was not able to process the content") em **28 das 88 operações**, incluindo um GET (`/postcode-lookup`, que responde mesmo 422 a um código postal inválido). O `GET /products` só documenta 200, 404 e 405, e o `page` é um inteiro sem mínimo definido. O esperado dos cenários da página inválida vem desta consistência, e está escrito na descrição da Regra.
- 🌍 **O ambiente público muda enquanto o exploras:** no Swagger, o `GET /products` devolveu `total` 50, depois 141 e depois 150, em poucos minutos (outras pessoas a criar produtos). Por isso os cenários nunca têm totais escritos à mão e correm, por omissão, na Toolshop local.
- 🔀 **O Swagger da Toolshop local envia os pedidos para o servidor público:** aberto em `localhost:8091`, o *Try it out* usa por omissão `api.practicesoftwaretesting.com`. Para falar com o Docker, é preciso escolher o servidor local na lista *Servers*, no topo da página. Só se dá por isso lendo o *Request URL*. **A causa:** a lista `servers` da especificação tem o endereço público **em primeiro lugar**, e o Swagger usa o primeiro por omissão.
- 🛡️ **Dependência vulnerável "escondida":** o REST Assured 6.3.0 traz consigo, como dependência transitiva, o `commons-codec 1.11`, que tem uma vulnerabilidade conhecida (WS-2019-0379). A versão 1.22.1 também tinha avisos (CVE-2026-119078 e CVE-2026-124077). A solução foi **forçar o `commons-codec 1.23.0`** no `dependencyManagement` do `pom.xml`, e os avisos desapareceram.

---

## 🗂️ Estrutura

```
Projeto-toolshop-java/
├── .editorconfig                 # regras de escrita do editor (LF, indentação)
├── .gitattributes                # o Git guarda com LF (exceto os .cmd, em CRLF)
├── .gitignore
├── .mvn/wrapper/                 # configuração do Maven Wrapper
├── mvnw / mvnw.cmd               # Maven Wrapper (Linux e macOS / Windows)
├── DIARIO.md                     # diário de bordo da aprendizagem
├── LICENSE
├── README.md
├── pom.xml                       # dependências (Maven)
├── docs/
│   ├── cabula-steps.md           # como pensar ao escrever um Quando / Então
│   └── comparacao-typescript-java.md   # o que muda do projeto 2 para o 3, e porquê
└── src/test/
    ├── java/io/github/evangelistaaj/
    │   ├── ExecutarCucumberTest.java     # runner normal (sem os @bug-conhecido)
    │   ├── ExecutarBugsConhecidos.java   # runner dos bugs conhecidos
    │   ├── steps/
    │   │   ├── Hooks.java                # @Before: o endereço da API (TOOLSHOP_API_URL)
    │   │   ├── ComumSteps.java           # passos comuns (o código da resposta)
    │   │   ├── MarcasSteps.java
    │   │   └── CatalogoSteps.java
    │   ├── unitarios/
    │   │   └── OraculosTest.java         # testes unitários das funções de oráculo (JUnit puro)
    │   └── support/
    │       ├── Contexto.java             # o "balcão" partilhado entre os passos (PicoContainer)
    │       └── Oraculos.java             # funções de oráculo (ex.: repetidos)
    └── resources/
        ├── cucumber.properties           # onde estão os passos (quando se corre pelo .feature)
        └── features/
            ├── marcas.feature
            └── catalogo.feature
```

---

## 🗺️ Plano

- [x] Projeto Maven com Java 21
- [x] Primeiro teste REST Assured (com as três provas)
- [x] Correção da dependência vulnerável
- [x] Cucumber-JVM ligado ao JUnit, com cenários em português
- [x] Separar os passos comuns (`ComumSteps`) dos passos de cada funcionalidade, com o `Contexto` partilhado (PicoContainer)
- [x] Testes a correr na Toolshop local (Docker), com o endereço configurável (`TOOLSHOP_API_URL`)
- [x] Correr os testes pela linha de comandos, com o Maven Wrapper (`mvnw test`)
- [x] Dois perfis: normal e bugs conhecidos
- [x] Testes unitários das funções de oráculo (JUnit), com sabotagem
- [x] Catálogo: ordenação (nome e preço) no catálogo inteiro
- [ ] Testes unitários dos oráculos da ordenação (com "Wood Saw" antes de "Wooden Workbench" como regressão)
- [ ] Mensagens de falha resumidas nos oráculos (o total e só os primeiros exemplos)
- [ ] Catálogo: pesquisa e filtros
- [ ] Trazer as restantes funcionalidades do projeto 2 (produtos, categorias, utilizadores, login…)
- [ ] Massa de teste em JSON (`dados/`)
- [ ] Contratos com JSON Schema
- [ ] Validar a ordenação por nome contra a própria base de dados (SQL com `ORDER BY name`), para o oráculo deixar de imitar as regras de ordenação
- [ ] Relatório HTML do Cucumber
- [ ] CI no GitHub Actions: a Toolshop em Docker na própria máquina da CI (a API fica em `localhost:8091`, por isso o endereço por omissão serve sem mudanças), Java 21 e o relatório no GitHub Pages

---

## 👤 Autor

**Arthur Evangelista**

- 🐙 GitHub: [@evangelistaaj](https://github.com/evangelistaaj)
- 📓 A jornada, passo a passo: [`DIARIO.md`](DIARIO.md)

> *"O erro está no sistema ou no meu teste?"* 🤔
