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
