# Baozi Store API REST

Atividade prática de Desenvolvimento Web Back End — UNINTER.

**Aluno:** Willian Douglas Ferreira · **RU:** 1813098  
**Curso:** CST Análise e Desenvolvimento de Sistemas — Distância (2701)

## Executar

Requisitos: JDK 17 ou superior e Apache Maven 3.6.3 ou superior.

```bash
mvn clean verify
mvn spring-boot:run
```

Alternativa após compilar: `java -jar target/baozi-store-1.0.0.jar`.
API local: `http://localhost:18130`. Não há interface web na raiz.
O H2 grava os dados em `data/baozi.mv.db`, criado automaticamente. O diretório `data/` não é versionado. A aplicação de exemplo escuta apenas na interface local.

## Estudo de caso

O cliente **Willian Douglas Ferreira1813098**, cadastrado em **12/11/2026**, compra **4 unidades** de **Baozi tradicional**, com preço unitário de **R$ 8,50** e `estoque: true`. Valor demonstrativo da compra: **R$ 34,00**. O total não é um campo persistido nem retornado pela API.

## Rotas

| Entidade | Criar | Listar | Consultar | Excluir |
| --- | --- | --- | --- | --- |
| Cliente | POST /clientes | GET /clientes | GET /clientes/{id} | DELETE /clientes/{id} |
| Produto | POST /produtos | GET /produtos | GET /produtos/{id} | DELETE /produtos/{id} |
| Pedido | POST /pedidos | GET /pedidos | GET /pedidos/{id} | DELETE /pedidos/{id} |

POST retorna **201** com corpo e cabeçalho Location. GET retorna **200**. DELETE retorna **204** sem corpo. Erros: **400** para dados inválidos; **404** para recurso inexistente; **409** para cliente/produto com pedido vinculado; **422** para produto indisponível.

`estoque` é um booleano de disponibilidade, conforme o enunciado. Não representa contagem de unidades. Cada pedido referencia exatamente um cliente e um produto. Não há cascata de exclusão: remova o pedido antes dos cadastros vinculados. PUT é opcional no enunciado e não foi implementado.

## Postman

Importe `postman/BaoziStore.postman_collection.json`. Execute as requisições na ordem. A variável `baseUrl` já aponta para localhost. Os IDs gerados são salvos automaticamente em `clienteId`, `produtoId` e `pedidoId`. Não suponha que IDs sempre começarão em 1.

A coleção inclui as 12 rotas obrigatórias e verificações de erro. **A execução completa apaga, ao final, somente os registros criados pela própria coleção**. Faça os prints dos POSTs, das listagens e das consultas antes dos DELETEs. Para o Postman sem login, use as requisições cURL em `postman/requisicoes.http` como referência e preencha os IDs devolvidos pelo servidor.

## Organização

- `model`: entidades JPA Cliente, Produto e Pedido.
- `repository`: interfaces Spring Data JPA.
- `controller`: endpoints REST e tratamento de erros.
- `service`: regras de cadastro, vínculos e disponibilidade.
- `dto`: contratos JSON e validação de entrada.
- `src/test`: testes de integração com H2 em memória, separados do banco de uso normal.

As respostas JSON são a representação REST; não há páginas HTML. O Spring MVC recebe as requisições nos controllers, os serviços aplicam as regras e os repositories persistem as entidades.
