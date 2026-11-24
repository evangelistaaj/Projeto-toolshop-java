# Diário de bordo — Projeto 3: API da Toolshop em Java + REST Assured

Uma entrada por sessão: o que se fez, o conceito novo, o bug (ou susto) mais interessante e uma frase para o LinkedIn.

---

## 2026-11-23 — Sessão 1: montar a oficina

**O que fiz**
- Decidi a stack do projeto 3: Java + REST Assured + Cucumber-JVM + JUnit, com Maven.
- Instalei o IntelliJ IDEA (com as definições importadas do VS Code).
- Criei o projeto Maven `projeto-toolshop-java` com o JDK Eclipse Temurin 21, descarregado pelo próprio IntelliJ.
- Corri o meu primeiro programa em Java (`Main.java`): `exit code 0` ✅

**Conceitos novos**
- **JDK**: o kit para compilar e correr Java. Usei o Temurin 21 (LTS).
- **Maven e `pom.xml`**: o `pom.xml` (Project Object Model) é o equivalente ao `package.json`. Tem a identidade do projeto (`groupId`, `artifactId`, `version`) e, em breve, as dependências.
- **groupId**: um domínio escrito ao contrário. Como uso o do GitHub, `evangelistaaj.github.io` passa a `io.github.evangelistaaj`.
- **Compilação**: o Java converte o `.java` em `.class` (na pasta `target/classes`) antes de correr. É parecido com o TypeScript, que passa a JavaScript.
- `System.out.println` = `console.log`.

**O susto do dia**
- `Hello and welcome!i = 1`: a primeira linha veio colada à segunda. Pergunta em aberto: porquê? (pista: `print` vs `println`)

**Frase para o LinkedIn**
> "Depois de dois projetos em TypeScript, chegou a vez do Java. Primeiro passo: descobrir que o `pom.xml` é só o `package.json` de terno e gravata. ☕👔"

---

## 2026-11-24 — Sessão 2: do primeiro teste ao Cucumber

**O que fiz**
- Corrigi uma vulnerabilidade: o REST Assured trazia o `commons-codec 1.11`. Forcei a 1.23.0 no `dependencyManagement` (a 1.22.1 também tinha avisos).
- Escrevi o meu primeiro teste em Java + REST Assured (GET `/brands` → 200), com as três provas: OK, Sabotagem (201) e NOK (marca inexistente → 404).
- Centralizei o endereço da API (`baseURI`) num só sítio.
- Liguei o Cucumber-JVM ao JUnit: `marcas.feature` em português, `MarcasSteps.java` e o runner `ExecutarCucumberTest`. ✅ 2 cenários a passar.
- Criei o repositório e publiquei no GitHub, com `.gitignore`, `.gitattributes`, `.editorconfig`, README e licença MIT.

**Conceitos novos**
- **Dependência transitiva**: a dependência de uma dependência (a "massa folhada" que já traz o sal).
- **BOM** (*Bill of Materials*): um pacote de versões compatíveis. É por isso que o `junit-jupiter` não precisa de `<version>`.
- **Classe, método, anotação** (`@Test`, `@BeforeAll`, `@Before`), `import` e `static`.
- **Falha vs. erro**: `AssertionError` = a verificação falhou; `UnknownHostException` = nem chegámos a falar com a API (o problema está no ambiente).
- **Ler um stack trace na diagonal**: a mensagem, a linha do meu `.java` e a linha do `.feature` (com o `✽`).
- **Cucumber em Java**: `.feature` + steps (o "dicionário") + runner; parâmetros `{string}` e `{int}`; um campo (`private Response resposta`) para partilhar a resposta entre o `Quando` e o `Então`.
- **LF vs. CRLF**: os finais de linha do Windows e do Linux; o `.gitattributes` e o `.editorconfig` trabalham em equipa.

**O bug mais interessante**
- Um **falso positivo** no meu próprio teste: escrevi `/marca-que-nao-existe` em vez de `/brands/marca-que-nao-existe`. A API respondia 404 na mesma, por isso o teste ficava verde sem testar marca nenhuma. Lição: um verde também se investiga.

**Frase para o LinkedIn**
> "Hoje o meu teste passou… pela razão errada. 😅 Foi aí que percebi porque é que, antes de confiar num verde, eu sabotei o meu próprio teste. Se ele não ficar vermelho quando eu o estrago, não está a testar nada."

### 2026-11-24 (tarde) — o restaurante, o Docker e o catálogo

**O que fiz**
- Separei os passos comuns: `ComumSteps` (o `Então recebo uma resposta com o código {int}`, a mesma frase do projeto 2), `Hooks` e a classe `Contexto`, partilhada pelo PicoContainer.
- Explorei o `GET /products` no Swagger antes de automatizar (só GETs).
- Pus o projeto a correr na Toolshop local (Docker) por omissão, com o endereço configurável pela variável `TOOLSHOP_API_URL`.
- Comecei o catálogo: ✅ "Consultar o catálogo", ✅ "A segunda página continua onde a primeira acabou" e ✅ "Uma página depois da última vem vazia". Cada um com OK e Sabotagem.
- Criei o `docs/comparacao-typescript-java.md`: o que muda do projeto 2 para o 3, e porquê, com uma secção de analogias.

**Conceitos novos**
- **Injeção de dependências** (PicoContainer) e **construtor**, explicados com o restaurante 🍽️: cada cenário é uma mesa; o cozinheiro (`MarcasSteps`, `CatalogoSteps`) pousa o prato (a resposta) no balcão (`Contexto`); o empregado (`ComumSteps`) confere-o; o gerente (PicoContainer) monta um balcão novo para cada mesa; o construtor é a entrada ao serviço.
- **`null` e `NullPointerException`**: ir conferir um prato que não está lá.
- **Variáveis de ambiente**: `System.getenv().getOrDefault(...)`, o endereço fora do código.
- **Três erros de ambiente** 📞: `UnknownHostException` (o número não existe), `Connection refused` (o telefone está desligado), `AssertionError` (atenderam, mas a resposta não era a esperada).
- **Olhar para dentro do JSON**: `.body("data.name", ...)` com matchers Hamcrest (`not(empty())`, `everyItem(instanceOf(...))`, `equalTo`, `hasSize`, `nullValue`); `queryParam`; `.extract().path(...)` para guardar um valor numa variável.
- **Headers da resposta**: `cache-control`, `etag`, CORS (`access-control-allow-origin: *`).
- **Comum ou da funcionalidade?** "Esta frase faria sentido noutro `.feature`?"
- **camelCase** 🐪: o nome do método é para as pessoas (aparece nos erros); o Cucumber usa o texto da anotação.

**O mais interessante**
- Duas descobertas na exploração: o catálogo público passou de **50 para 141 e depois para 150 produtos em poucos minutos** (ambiente partilhado), e o **Swagger aberto no `localhost` enviava os pedidos para o servidor público**. Só dei por isso porque li o *Request URL*.

**Frase para o LinkedIn**
> "Abri o Swagger no meu `localhost`, carreguei em Execute… e o pedido foi para o servidor público. 🤯 O 'Request URL' estava ali o tempo todo. Lição do dia: um QA não lê só a resposta, lê também a pergunta."

**Pendências para a próxima sessão**
- 🧰 Catálogo: percorrer todas as páginas (cada produto uma só vez), pesquisa, filtros e ordenação (os oráculos "a sério", comparados com o catálogo completo).
- 🔧 Maven Wrapper (`mvnw`) e `mvn test`, a preparar a CI.
- ❓ Em aberto desde a sessão 1: porque é que o `Hello and welcome!` ficou colado ao `i = 1`? (`print` vs `println`)
