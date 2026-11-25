# 🔄 Do TypeScript ao Java: o que muda do projeto 2 para o projeto 3

Documento vivo: cresce a cada bloco do projeto. Para cada tema, o que se faz em cada lado e **porquê**.

- **Projeto 2:** [Projeto-toolshop-api](https://github.com/evangelistaaj/Projeto-toolshop-api) (TypeScript + Playwright + Cucumber-JS)
- **Projeto 3:** este (Java + REST Assured + Cucumber-JVM + JUnit)

---

## 📦 Projeto e dependências

| Tema | Projeto 2 (TypeScript) | Projeto 3 (Java) | Porquê a diferença |
|---|---|---|---|
| Lista de dependências | `package.json` | `pom.xml` (Maven) | Cada ecossistema tem o seu gestor: npm no JavaScript, Maven (ou Gradle) no Java. A ideia é a mesma: identidade do projeto + bibliotecas. |
| Formato | JSON | XML (`<tags>`) | O Maven é de 2004, do tempo em que o XML era o padrão. |
| A versão da própria ferramenta | Node instalado à mão (a versão vem no README) | **Maven Wrapper** (`mvnw`): um script dentro do projeto que descarrega a versão certa do Maven | Toda a gente (e a CI) usa o mesmo Maven, sem instalar nada. O `mvnw.cmd` (Windows) tem de ficar em **CRLF**, por isso há uma exceção no `.gitattributes`. |
| Instalar | `npm install` | Reload do Maven (`Ctrl+Shift+O` no IntelliJ) | O IntelliJ chama o Maven por ti. **Esquecer o reload** dá "dependency not found". |
| Onde ficam as bibliotecas | `node_modules/`, dentro de **cada** projeto | `~/.m2/repository/`, **partilhada** por todos os projetos Java | O Maven descarrega cada versão uma só vez para o computador inteiro. Por isso o projeto Java não tem uma pasta pesada lá dentro. |
| Dependências das dependências | Vêm sozinhas (centenas de pastas no `node_modules`) | Vêm sozinhas (**dependências transitivas**) | Igual nos dois lados. Uma transitiva pode trazer uma vulnerabilidade: no projeto 3, o `commons-codec 1.11` veio "dentro" do REST Assured. |
| Forçar a versão de uma transitiva | `overrides` no `package.json` | `<dependencyManagement>` no `pom.xml` | É o mesmo objetivo: "se alguém precisar disto, usa esta versão". |
| Versões que se dão bem entre si | `package-lock.json` fixa as versões **exatas** que foram instaladas | **BOM** (*Bill of Materials*): um "pacote" de versões compatíveis de uma família (JUnit, Cucumber) | Não são a mesma coisa: o lock **congela** o que foi instalado; o BOM **combina** versões de uma família, e por isso as dependências dessa família ficam sem `<version>`. |

## ☕ A linguagem

| Tema | Projeto 2 (TypeScript) | Projeto 3 (Java) | Porquê a diferença |
|---|---|---|---|
| Compilação | O TypeScript passa a JavaScript (ou corre com o `ts-node`) | O `.java` passa a `.class`, na pasta `target/` (ignorada no Git) | Os dois são compilados. Em Java não há como fugir: sem compilar, não corre. |
| Onde vive o código | Funções e ficheiros soltos | **Tudo dentro de uma classe** (e o nome da classe = o nome do ficheiro) | O Java é orientado a objetos "à força". A classe é a caixa que agrupa tudo. |
| Ponto e vírgula | Opcional | **Obrigatório** | Regra da linguagem. Esquecer um `;` é erro de compilação. |
| Tipos | Opcionais, muitas vezes adivinhados | **Obrigatórios**: `String id`, `int statusEsperado`, `Response resposta` | O Java quer saber sempre o tipo de cada coisa. O IntelliJ mostra-os a cinzento quando não os escreves. |
| Listas | `const ids: string[] = []`, `ids.push(...)`, `ids.length` | `List<String> ids = new ArrayList<>()`, `ids.addAll(...)`, `ids.size()` | Em Java diz-se **o tipo** do que vai na lista (`<String>`) e escolhe-se a "implementação" (`ArrayList`). |
| Encontrar repetidos | `lista.filter((e, i) => lista.indexOf(e) !== i)` | um `Set` (porteiro com memória 🚪): `vistos.add(e)` devolve `false` se já lá estava | As duas funcionam; o `Set` é a forma típica do Java e é mais rápido em listas grandes. |
| Verificar um valor "solto" | `expect(ids.length, 'mensagem').toBe(total)` | `assertThat("mensagem", ids.size(), equalTo(total))` | No Java a **mensagem vem primeiro**. |
| Escrever no ecrã | `console.log(...)` | `System.out.println(...)` | É só o nome. |
| Imports | `import { x } from '...'` | `import pacote.Classe;` e `import static ...` (para usar `given()` sem o nome da classe) | Em Java, sem import, aparece **`cannot find symbol`**. O `Alt+Enter` do IntelliJ escreve-os por ti. |
| Pastas de código | Livres (`steps/`, `support/`…) | **Convenção Maven**: `src/main/java` (código da aplicação) e `src/test/java` + `src/test/resources` (testes e ficheiros não-Java, como os `.feature`) | O Maven espera esta "planta da casa", e toda a gente a segue. |
| Pacotes | Caminhos de ficheiros | `package io.github.evangelistaaj...` = domínio ao contrário | Evita que duas bibliotecas tenham classes com o mesmo nome completo. |

## 🌐 Pedidos à API

| Tema | Projeto 2 (TypeScript) | Projeto 3 (Java) | Porquê a diferença |
|---|---|---|---|
| Biblioteca | Playwright (`APIRequestContext`) | **REST Assured** | O REST Assured é a biblioteca de referência para testar APIs em Java, e escreve-se como o Gherkin: `given().when().get(...).then()`. |
| Endereço base | `baseURL` na configuração | `RestAssured.baseURI = "..."`, num hook `@Before` | Escreve-se uma só vez; os passos usam só o caminho (`/brands`). |
| Olhar para dentro do JSON | `const corpo = await resposta.json()` e depois `corpo.data`, `corpo.data.map(p => p.name)` | `.body("data.name", ...)` | No REST Assured, o caminho (`"data.name"`) faz o trabalho do `map`: "entra em cada produto e tira-lhe o `name`". |
| Verificar valores | `expect(...).toEqual(expect.objectContaining({ price: expect.any(Number) }))` | Matchers **Hamcrest**: `everyItem(instanceOf(Number.class))`, `not(empty())` | São frases quase em inglês, e a mensagem de falha escreve-se sozinha ("Expected: every item is an instance of String / Actual: [14.15, ...]"). |
| Endereço configurável | Variável de ambiente lida com `process.env` | `System.getenv().getOrDefault("TOOLSHOP_API_URL", "http://localhost:8091")` | A mesma ideia: o endereço vive **fora do código**. O `getOrDefault` dá um valor por omissão quando a variável não existe. |
| Verificar o status | `expect(resposta.status()).toBe(200)` | `resposta.then().statusCode(200)` | No REST Assured, a verificação faz parte da "frase" da resposta. |

## 🥒 Cucumber

| Tema | Projeto 2 (TypeScript) | Projeto 3 (Java) | Porquê a diferença |
|---|---|---|---|
| Configuração | `cucumber.js` | **Runner** (`ExecutarCucumberTest`, uma classe vazia com anotações `@Suite`…) + `cucumber.properties` | Em Java, o Cucumber corre "por dentro" do JUnit. O runner diz ao JUnit "usa o motor do Cucumber, os cenários estão aqui, os passos estão ali". O `cucumber.properties` serve para quando corres pelo `.feature`. |
| Passos | Funções: `When('frase', async function () {...})` | Métodos com anotações: `@Quando("frase") public void ...() {...}` | Em Java, a "etiqueta" que liga a frase ao código é uma **anotação**. |
| Nome dos passos | Função **sem nome**: `Then('a página vem vazia', async function () {...})` | Método **com nome**: `public void aPaginaVemVazia()` | Em Java, todo o método tem nome. O Cucumber **ignora-o** (quem liga a frase ao código é o texto da anotação), mas o nome aparece nas **mensagens de erro**. Convenção: a frase em **camelCase** 🐪, sem acentos. |
| Passo comum ou da funcionalidade? | `comum.steps.ts` só com 2 frases (código da resposta e contrato) | `ComumSteps` com a mesma regra | Pergunta: "esta frase faria sentido noutro `.feature`?" Só fala da resposta HTTP → **comum**. Usa palavras do negócio (página, produto, marca) → **funcionalidade**. Se outra funcionalidade precisar dela, muda-se nessa altura. |
| Correr | `npx cucumber-js` (ou `npm test`) | ▶️ no runner ou no `.feature` (IntelliJ), ou `.\mvnw.cmd test` no terminal | A classe dos passos não tem ▶️: é um dicionário, não um teste. |
| Perfis (normal / bugs) | Perfis no `cucumber.js`, escolhidos com `npm test` e `npm run test:bugs` | **Um runner por perfil**: `@ExcludeTags("bug-conhecido")` no normal e `@IncludeTags("bug-conhecido")` no dos bugs | O filtro do JUnit é um **porteiro à porta** 🎭: quem não está na lista nem entra (0 saltados). O filtro do Cucumber (`cucumber.filter.tags`) é um arrumador dentro da sala: deixa entrar todos e marca os outros como "saltados". As tags escrevem-se **sem o `@`** no JUnit. |
| Testes unitários dos oráculos | `testes/unitarios/` com o executor de testes do próprio Node (`node --test`, via `npm run test:unit`) | `unitarios/OraculosTest.java` com **JUnit puro** (`@Test`, `@DisplayName`), no mesmo `mvnw test` | Em Java, o JUnit corre os dois tipos de teste juntos: os cenários (pelo runner) e os unitários (pelo nome acabado em `Test`). |
| Hooks | `hooks.ts`, com `Before(...)` | Classe `Hooks` com `@Before` (atenção: `io.cucumber.java.Before`, **não** o do JUnit) | O mesmo conceito. Em Java há dois `@Before` com o mesmo nome, e o import decide qual é. |
| Partilhar dados entre passos | **World** (`this` dentro dos passos) | Classe **`Contexto`** (a "mochila") + **PicoContainer** | Em Java, cada classe de passos tem a sua própria memória. O PicoContainer cria **uma mochila nova por cenário** e entrega a mesma a todas as classes que a pedirem. No projeto 2, o Cucumber-JS fazia isso sozinho com o `this`. |

## 🐞 Erros

| Tema | Projeto 2 (TypeScript) | Projeto 3 (Java) | Porquê a diferença |
|---|---|---|---|
| Verificação falhou | `expect` falha, com "Expected… Received…" | `AssertionError`: "Expected status code <201> but was <200>" | O mesmo tipo de falha: a API respondeu, mas não o que esperávamos. |
| Nem chegou a falar com a API | Erro de rede do Node | Exceção, por exemplo `UnknownHostException` | Não é uma falha da verificação: é um **erro** (o problema costuma estar no ambiente). |
| Ninguém atende na porta | `ECONNREFUSED` | `ConnectException: Connection refused` | O endereço existe, mas a aplicação está parada (ex.: o Docker desligado). |
| Ler o erro | Stack trace do Node | Stack trace Java (longo!) | Truque: ler só 3 linhas. A **mensagem** (o quê), a linha com **o teu `.java`** (onde no código) e a linha com **`✽` e o `.feature`** (que passo). |

## 🧰 Ferramentas

| Tema | Projeto 2 (TypeScript) | Projeto 3 (Java) | Porquê a diferença |
|---|---|---|---|
| Editor | VS Code | IntelliJ IDEA | O IntelliJ é o padrão nas equipas Java: descarrega o JDK, traz o Maven e liga o Gherkin ao Java (plugin *Cucumber for Java*). |
| Finais de linha | `.gitattributes` + `.editorconfig` | Os mesmos | É igual: não depende da linguagem. |

---

## 🧠 Analogias

As imagens do dia a dia que ajudaram a perceber cada conceito. Servem para estudar e para explicar a outras pessoas.

| Conceito | Analogia |
|---|---|
| `pom.xml` / `package.json` | 🛒 A **lista de compras** do projeto: o que o Maven (ou o npm) tem de ir buscar. |
| `<dependencies>` vs `<dependencyManagement>` | 🛒 O que **compras** vs. as **regras da compra** ("se precisares disto, compra esta versão"). |
| Dependência transitiva | 🥐 Compras **massa folhada** já feita; vem com farinha, manteiga e sal, que nunca puseste na lista. Se o sal for de um lote com problemas, o teu pastel leva-o na mesma. |
| BOM | 📦 Um **pacote** de versões que se dão bem umas com as outras: escolhes o pacote e ele preenche as versões das peças. |
| Compilação (`.java` → `.class`) | 📝 Passar o rascunho a limpo, numa língua que a máquina lê, antes de o usar. |
| Classe de passos (steps) | 📖 Um **dicionário**: traduz cada frase em português para código. Não se "corre" sozinho, fica à espera que o consultem. |
| Runner | ▶️ **Quem corre**: lê o `.feature` e, para cada frase, vai ao dicionário buscar o código. |
| Cenário | 🍽️ **Uma mesa** do restaurante, com um pedido. |
| Passos de cada funcionalidade (`MarcasSteps`) | 👨‍🍳 O **cozinheiro**: prepara o prato (faz o pedido à API). |
| Passos comuns (`ComumSteps`) | 🧑‍💼 O **empregado**: confere o prato antes de o servir (verifica a resposta). Serve todas as mesas. |
| `Contexto` (World no projeto 2) | 🪵 O **balcão** (ou a mochila) onde o cozinheiro pousa o prato e o empregado o vai buscar. |
| PicoContainer | 👔 O **gerente**: monta um balcão novo e limpo para cada mesa e diz a todos "hoje trabalham neste". |
| Construtor | 🕘 O **momento em que cada um entra ao serviço** e o gerente lhe diz qual é o balcão. |
| `null` / `NullPointerException` | 🍽️❌ Ir conferir um prato que **não está lá**: "tentei usar X, mas X era nada". A pergunta seguinte é sempre: porque é que estava vazio? |
| `UnknownHostException` / `Connection refused` / `AssertionError` | 📞 Ligar para um número que **não existe** / o número existe, mas o telefone está **desligado** / atenderam, mas a resposta **não era a esperada**. |
| Sabotagem | 🔧 Estragar o teste de propósito: se não ficar vermelho, não estava a verificar nada. |
| Testes unitários de um oráculo | ⚖️ **Testar a balança** com pesos conhecidos: "este prato pesa 1 kg; a balança diz 1 kg?" E também com o prato vazio: uma balança que diz sempre "zero" só é apanhada com um peso a sério. |
| Falso positivo | ✅🤥 Um teste verde **pela razão errada** (ex.: um endereço inventado também dá 404). Um verde também se investiga. |
