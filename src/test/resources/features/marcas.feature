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
      Então recebo uma resposta com o código 200

    Cenário: Pedir uma marca que não existe
      Quando peço a marca com o id "marca-que-nao-existe"
      Então recebo uma resposta com o código 404
