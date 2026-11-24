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
