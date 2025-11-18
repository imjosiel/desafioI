# DesafioI – CRUD com Spring Boot, JPA e PostgreSQL

## Tecnologias
- Java 17
- Spring Boot 3 (Web, Data JPA, Validation, Actuator)
- PostgreSQL

## Requisitos atendidos
- CRUD (Create/Read/Update/Delete/List) de catálogo (produto/serviço), pedidos e itens de pedido
- IDs `UUID` em todas as entidades
- Diferenciação produto/serviço via campo `tipo` (`PRODUTO` | `SERVICO`)
- Desconto percentual aplicado exclusivamente sobre o total de produtos

## Configuração
- Crie um arquivo `.env` na raiz (o projeto já importa automaticamente):
```
DB_URL=jdbc:postgresql://localhost:5432/desafio?sslmode=require
DB_USERNAME=postgres
DB_PASSWORD=<sua_senha>
```
- O projeto inclui `.env.example` como referência (sem segredos). Não versione o `.env`.

## Como executar
- Dev (recomendado):
```
mvn spring-boot:run
```
- Jar (após build):
```
mvn -B clean package
java -jar target/desafioI-0.0.1-SNAPSHOT.jar
```
- Health check:
```
GET http://localhost:8080/actuator/health
```

## Testes
- Executar testes:
```
mvn -B test
```

## Endpoints principais
- Catálogo (`/api/catalogo`)
  - POST: cria item
  - GET: lista todos
  - GET `/{id}`: busca por id
  - PUT `/{id}`: atualiza
  - DELETE `/{id}`: remove
- Pedidos (`/api/pedidos`)
  - POST: cria pedido com itens e desconto
  - GET: lista todos
  - GET `/{id}`: busca por id
  - PUT `/{id}`: atualiza itens e desconto
  - DELETE `/{id}`: remove
- Itens de pedido
  - POST `/api/pedidos/{pedidoId}/itens`: adiciona item
  - PUT `/api/pedidos/{pedidoId}/itens/{itemId}`: atualiza item
  - DELETE `/api/pedidos/{pedidoId}/itens/{itemId}`: remove item

## Exemplos (PowerShell)
- Criar produto:
```
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/catalogo -ContentType 'application/json' -Body '{"nome":"Notebook","preco":3500.00,"tipo":"PRODUTO"}'
```
- Criar serviço:
```
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/catalogo -ContentType 'application/json' -Body '{"nome":"Instalacao","preco":200.00,"tipo":"SERVICO"}'
```
- Criar pedido com desconto e itens (substitua pelos UUIDs gerados):
```
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/pedidos -ContentType 'application/json' -Body '{"descontoPercentual":10,"itens":[{"produtoServicoId":"<uuid-produto>","quantidade":2},{"produtoServicoId":"<uuid-servico>","quantidade":1}]}'
```
- Atualizar pedido:
```
Invoke-RestMethod -Method Put -Uri http://localhost:8080/api/pedidos/<pedidoId> -ContentType 'application/json' -Body '{"descontoPercentual":5,"itens":[{"produtoServicoId":"<uuid-produto>","quantidade":1}]}'
```

## Regras de desconto
- Implementadas no serviço de pedidos:
  - Total de produtos e serviços é calculado somando subtotais de itens (preço unitário × quantidade)
  - O desconto percentual aplica apenas sobre o total de produtos
  - Total final = `(totalProdutos - totalDesconto) + totalServicos`

## Segurança
- `.env` está ignorado via `.gitignore`. Use `/.env.example` como guia.