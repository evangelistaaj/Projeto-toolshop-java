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
