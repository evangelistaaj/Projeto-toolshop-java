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
