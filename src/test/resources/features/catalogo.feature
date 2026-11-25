# language: pt
Funcionalidade: Catálogo de produtos
  Como cliente da Toolshop
  Quero consultar o catálogo de produtos
  Para escolher as ferramentas de que preciso

  Cenário: Consultar o catálogo
    Quando consulto o catálogo de produtos
    Então recebo uma lista de produtos


  Regra: O catálogo é paginado, com 9 produtos por página
  A resposta indica a página, a posição dos produtos no catálogo ("do 10.º
  ao 18.º"), o total de produtos e a última página. O total muda de ambiente
  para ambiente, por isso os cenários usam o que a própria resposta indica.

    Cenário: A segunda página continua onde a primeira acabou
      Quando consulto a página 2 do catálogo
      Então a página 2 tem 9 produtos, do 10.º ao 18.º

    Cenário: Uma página depois da última vem vazia
      Quando consulto a página a seguir à última do catálogo
      Então a página vem vazia

    Esquema do Cenário: Percorrer o catálogo, com a ordem <ordem>, devolve cada produto uma só vez
      Quando percorro todas as páginas do catálogo, com a ordem "<ordem>"
      Então recebo cada produto do catálogo uma só vez

      Exemplos:
        | ordem             |
        | por omissão       |
        | nome crescente    |
        | preço decrescente |


  Regra: Um número de página que não é válido é recusado
  O Swagger define a página como um número inteiro, sem dizer a partir de
  que número, e não documenta o 422 no GET /products. Esperamos 422 porque
  é a resposta que a própria API documenta noutras 28 operações para um
  conteúdo que não consegue processar, incluindo um GET (/postcode-lookup,
  que responde 422 a um código postal inválido). Nunca 200 com outra página.

    Esquema do Cenário: Uma página com <problema> é recusada
      Quando consulto "<endereço>" com "<parâmetros>"
      Então recebo uma resposta com o código 422

      @bug-conhecido @issue-28
      Exemplos: Abaixo do limite
      Bug: os parâmetros do catálogo não são validados; o pedido é aceite com 200.
      https://github.com/evangelistaaj/Projeto-toolshop-api/issues/28

        | problema           | endereço  | parâmetros |
        | o número zero      | /products | page=0     |
        | um número negativo | /products | page=-1    |

      @bug-conhecido @issue-28
      Exemplos: Não é um número inteiro
      Bug: os parâmetros do catálogo não são validados; o pedido é aceite com 200.
      https://github.com/evangelistaaj/Projeto-toolshop-api/issues/28

        | problema          | endereço  | parâmetros |
        | letras            | /products | page=abc   |
        | um número decimal | /products | page=1.5   |
