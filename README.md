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

| # | Projeto | Stack |
|---|---|---|
| 1 | [Projeto-toolshop](https://github.com/evangelistaaj/Projeto-toolshop) (UI) | Playwright + playwright-bdd + TypeScript |
| 2 | [Projeto-toolshop-api](https://github.com/evangelistaaj/Projeto-toolshop-api) (API) | Cucumber + Playwright + TypeScript |
| **3** | **Projeto-toolshop-java (API)** | **Java + REST Assured + Cucumber-JVM + JUnit** |

**Porquê o mesmo sistema outra vez?** Porque eu já conheço o domínio de trás para a frente: marcas, produtos, utilizadores, login… Assim, toda a energia vai para aprender a stack que o mercado mais pede em Portugal para *Automation Tester*: **Java + REST Assured + Cucumber**.

E, desta vez, **o código é escrito por mim, linha a linha**. 💪

---

## 🛠️ Tecnologias

| Ferramenta | Versão | Para quê |
|---|---|---|
| ☕ Java (Eclipse Temurin) | 21 (LTS) | linguagem |
| 📦 Maven | (incluído no IntelliJ) | dependências e build |
| 🌐 REST Assured | 6.3.0 | pedidos HTTP e verificações (`given / when / then`) |
| 🥒 Cucumber-JVM | 8.0.2 | cenários em Gherkin, em português |
| ✅ JUnit Platform | 6.1.3 | motor de execução dos testes |
| 💡 IntelliJ IDEA | 2026.3 | editor (com o plugin *Cucumber for Java*) |

---

## ▶️ Como executar

**Pré-requisitos:** IntelliJ IDEA com o plugin **Cucumber for Java**. O próprio IntelliJ descarrega o JDK 21 e já traz o Maven.

1. Clonar o repositório:
   ```bash
   git clone https://github.com/evangelistaaj/Projeto-toolshop-java.git
   ```
2. Abrir a pasta no IntelliJ e deixar o Maven descarregar as dependências.
3. Correr os cenários, de uma destas formas:
   - **Todos:** ▶️ ao lado de `ExecutarCucumberTest` (o runner).
   - **Uma funcionalidade ou um cenário:** ▶️ dentro do ficheiro `.feature`.

> ℹ️ Por agora, os testes correm contra a **API pública** da Toolshop e **só fazem leituras** (GET). Os cenários que escrevem dados vão correr só na versão local da aplicação, em Docker.

---

## 🥒 Exemplo de cenário

```gherkin
# language: pt
Funcionalidade: Marcas
  Como cliente da Toolshop
  Quero consultar as marcas disponíveis
  Para encontrar os produtos da marca que prefiro

  Regra: Só se consultam marcas que existem
    A lista de marcas está aberta a todos.
    Pedir uma marca que não existe devolve "não encontrado".

    Cenário: Listar todas as marcas
      Quando peço a lista de marcas
      Então a resposta tem o status 200

    Cenário: Pedir uma marca que não existe
      Quando peço a marca com o id "marca-que-nao-existe"
      Então a resposta tem o status 404
```

E o "dicionário" que liga o português ao Java:

```java
@Quando("peço a marca com o id {string}")
public void pecoAMarcaComOId(String id) {
    resposta = given().when().get("/brands/" + id);
}

@Então("a resposta tem o status {int}")
public void aRespostaTemOStatus(int statusEsperado) {
    resposta.then().statusCode(statusEsperado);
}
```

---

## 📊 Estado atual

| Funcionalidade | Cenários | Estado |
|---|---|---|
| 🏷️ Marcas | 2 | ✅ a passar |

🚧 Projeto em construção: os cenários do [projeto 2](https://github.com/evangelistaaj/Projeto-toolshop-api) vão chegar aqui, funcionalidade a funcionalidade.

---

## 🔍 O que é verificado

- **Marcas**
  - ✅ A lista de marcas responde com status **200**.
  - ✅ Pedir uma marca que não existe responde com **404** (caso negativo).

---

## 🧠 Boas práticas e decisões

- **Três provas para cada verificação nova:**
  - **OK**: o caso feliz passa.
  - **NOK**: o caso negativo comporta-se como deve.
  - **Sabotagem**: altero o código de propósito para o teste ficar vermelho, e depois desfaço. Se a sabotagem não for apanhada, o teste não está a verificar nada.
- **Desconfiar dos verdes:** um teste pode passar pela razão errada (*falso positivo*). Exemplo real deste projeto: um endereço mal escrito também devolvia 404, por isso o teste "da marca inexistente" passava sem testar marca nenhuma. A sabotagem e a revisão do código apanharam-no.
- **Gherkin declarativo, em português:** um só `Quando` por cenário, `Regra:` com descrição e passos reutilizáveis com parâmetros (`{string}`, `{int}`).
- **Endereço da API num só sítio:** um *hook* `@Before` do Cucumber define o `baseURI`, e os passos usam só o caminho (`/brands`).
- **Versões geridas por BOM:** o JUnit e o Cucumber usam BOMs (*Bill of Materials*) no `dependencyManagement`, para as versões de cada família de bibliotecas serem compatíveis entre si.
- **Finais de linha consistentes:** o `.editorconfig` faz o editor escrever com LF, e o `.gitattributes` faz o Git guardar com LF, em qualquer sistema operativo.

---

## 🐞 Bugs encontrados

Ainda nenhum confirmado neste projeto. Os bugs só passam a *issue* depois de um teste os confirmar.

---

## 💡 Descobertas

- 🛡️ **Dependência vulnerável "escondida":** o REST Assured 6.3.0 traz consigo, como dependência transitiva, o `commons-codec 1.11`, que tem uma vulnerabilidade conhecida (WS-2019-0379). A versão 1.22.1 também tinha avisos (CVE-2026-119078 e CVE-2026-124077). A solução foi **forçar o `commons-codec 1.23.0`** no `dependencyManagement` do `pom.xml`, e os avisos desapareceram.

---

## 🗂️ Estrutura

```
Projeto-toolshop-java/
├── .editorconfig                 # regras de escrita do editor (LF, indentação)
├── .gitattributes                # o Git guarda sempre com LF
├── .gitignore
├── DIARIO.md                     # diário de bordo da aprendizagem
├── LICENSE
├── README.md
├── pom.xml                       # dependências (Maven)
└── src/test/
    ├── java/io/github/evangelistaaj/
    │   ├── ExecutarCucumberTest.java   # runner: liga o Cucumber ao JUnit
    │   └── steps/
    │       └── MarcasSteps.java        # passos (o "dicionário" português → Java)
    └── resources/
        ├── cucumber.properties         # onde estão os passos (glue)
        └── features/
            └── marcas.feature          # cenários em Gherkin
```

---

## 🗺️ Plano

- [x] Projeto Maven com Java 21
- [x] Primeiro teste REST Assured (com as três provas)
- [x] Correção da dependência vulnerável
- [x] Cucumber-JVM ligado ao JUnit, com cenários em português
- [ ] Separar os passos comuns (`ComumSteps`) dos passos de cada funcionalidade
- [ ] Trazer as restantes funcionalidades do projeto 2 (produtos, categorias, utilizadores, login…)
- [ ] Massa de teste em JSON (`dados/`)
- [ ] Contratos com JSON Schema
- [ ] Testes unitários das funções de oráculo
- [ ] Aplicação local em Docker para os cenários que escrevem dados
- [ ] Relatório HTML do Cucumber
- [ ] CI no GitHub Actions, com o relatório no GitHub Pages

---

## 👤 Autor

**Arthur Evangelista** 

- 🐙 GitHub: [@evangelistaaj](https://github.com/evangelistaaj)
- 📓 A jornada, passo a passo: [`DIARIO.md`](DIARIO.md)

> *"O erro está no sistema ou no meu teste?"* 🤔
