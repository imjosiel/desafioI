# desafioI

Sistema de cadastro de catálogo (produto/serviço), pedidos e itens de pedido com regra de desconto aplicada somente sobre produtos. Implementado com Spring Boot, JPA e PostgreSQL.

## Requisitos

- Java 17 (compatível com 8+)
- Maven Wrapper (já incluso no projeto)
- Banco de dados PostgreSQL (local ou gerenciado, ex.: Neon)

## Arquitetura

- Ponto de entrada: `src/main/java/br/com/erp/desafioI/DesafioIApplication.java:6-11`
- Entidades:
  - `ProdutoServico`: `src/main/java/br/com/erp/desafioI/domain/ProdutoServico.java`
  - `Pedido`: `src/main/java/br/com/erp/desafioI/domain/Pedido.java`
  - `ItemPedido`: `src/main/java/br/com/erp/desafioI/domain/ItemPedido.java`
  - Enum `TipoItem`: `src/main/java/br/com/erp/desafioI/domain/TipoItem.java`
- Repositórios:
  - `ProdutoServicoRepository`: `src/main/java/br/com/erp/desafioI/repository/ProdutoServicoRepository.java`
  - `PedidoRepository`: `src/main/java/br/com/erp/desafioI/repository/PedidoRepository.java`
  - `ItemPedidoRepository`: `src/main/java/br/com/erp/desafioI/repository/ItemPedidoRepository.java`
- Serviços:
  - `ProdutoServicoService`: `src/main/java/br/com/erp/desafioI/service/ProdutoServicoService.java`
  - `PedidoService`: `src/main/java/br/com/erp/desafioI/service/PedidoService.java`
- Controllers REST:
  - `ProdutoServicoController`: `src/main/java/br/com/erp/desafioI/controller/ProdutoServicoController.java`
  - `PedidoController`: `src/main/java/br/com/erp/desafioI/controller/PedidoController.java`
- Tratamento de erros: `src/main/java/br/com/erp/desafioI/exception/ApiExceptionHandler.java`

## Configuração

Arquivo `src/main/resources/application.properties` aceita variáveis de ambiente:

```
DB_URL=jdbc:postgresql://localhost:5432/desafio
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

- `spring.jpa.hibernate.ddl-auto=update` cria/atualiza as tabelas automaticamente.
- Para banco gerenciado (Neon), use `sslmode=require` na URL JDBC.

### Exemplo de configuração Neon

```
DB_URL=jdbc:postgresql://<host>/<database>?sslmode=require
DB_USERNAME=<usuario>
DB_PASSWORD=<senha>
```

## Executar

Windows (PowerShell):

```
# Defina variáveis de ambiente conforme seu banco
$env:DB_URL="jdbc:postgresql://localhost:5432/desafio"; $env:DB_USERNAME="postgres"; $env:DB_PASSWORD="postgres"

# Executar com Maven Wrapper
.\mvnw.cmd spring-boot:run
```

Ou via JAR após empacotar:

```
# Empacotar
.\mvnw.cmd -DskipTests=true package

# Rodar JAR
& "$env:JAVA_HOME\bin\java.exe" -jar target\desafioI-0.0.1-SNAPSHOT.jar
```

## Testes

- Unitário da regra de desconto: `src/test/java/br/com/erp/desafioI/service/PedidoServiceTest.java`
- Integração (persistência no banco): `src/test/java/br/com/erp/desafioI/integration/NeonIntegrationTest.java`

Executar testes:

```
.\mvnw.cmd test
```

## Mapeamento de Fluxo

- Entrada HTTP em controllers chama serviços de domínio.
- Serviços coordenam repositórios JPA e aplicam regras.
- Repositórios persistem entidades (`ProdutoServico`, `Pedido`, `ItemPedido`).
- Respostas usam DTOs para exposição dos dados e totais.

## Endpoints

- Catálogo
  - `POST /api/catalogo` cria produto/serviço
  - `GET /api/catalogo` lista
  - `GET /api/catalogo/{id}` detalhes
  - `PUT /api/catalogo/{id}` atualiza
  - `DELETE /api/catalogo/{id}` remove

- Pedidos
  - `POST /api/pedidos` cria pedido com itens
  - `GET /api/pedidos` lista pedidos
  - `GET /api/pedidos/{id}` detalhes
  - `PUT /api/pedidos/{id}` atualiza itens e desconto
  - `DELETE /api/pedidos/{id}` remove

- Itens de pedido
  - `POST /api/pedidos/{pedidoId}/itens` adiciona item
  - `PUT /api/pedidos/{pedidoId}/itens/{itemId}` atualiza item
  - `DELETE /api/pedidos/{pedidoId}/itens/{itemId}` remove item

### Exemplos de payload

Criar produto (PRODUTO):

```
{
  "nome": "Notebook",
  "preco": 3500.00,
  "tipo": "PRODUTO"
}
```

Criar serviço (SERVICO):

```
{
  "nome": "Instalacao",
  "preco": 150.00,
  "tipo": "SERVICO"
}
```

Criar pedido com desconto (apenas sobre produtos):

```
{
  "descontoPercentual": 10,
  "itens": [
    {"produtoServicoId": "<UUID-produto>", "quantidade": 2},
    {"produtoServicoId": "<UUID-servico>", "quantidade": 1}
  ]
}
```

### Regra de desconto

- `totalProdutos = soma(subtotal) dos itens com tipo PRODUTO`
- `totalServicos = soma(subtotal) dos itens com tipo SERVICO`
- `totalDesconto = totalProdutos * (descontoPercentual / 100)` (com arredondamento `HALF_UP` para 2 casas)
- `totalFinal = (totalProdutos - totalDesconto) + totalServicos`

## Validações e Erros

- Validações:
  - `ProdutoServicoDTO`: nome obrigatório, preço positivo, tipo obrigatório
  - `ItemPedidoDTO`: `produtoServicoId` obrigatório, `quantidade >= 1`
  - `PedidoRequest`: `descontoPercentual` entre 0 e 100, itens obrigatórios
- Erros:
  - Respostas em JSON padronizadas por `ApiExceptionHandler`, com mensagens de erro e detalhes de validação.

## Coleções para teste (Postman/Insomnia)

- Pasta: `curls para teste`
  - Postman: `curls para teste/postman_collection.json`
  - Insomnia: `curls para teste/insomnia_collection.json`
  - Configure `baseUrl` e atualize as variáveis de IDs conforme criar os recursos.

## Troubleshooting

- `JAVA_HOME` não definido: instale JDK 17 e ajuste variáveis de ambiente.
- Falha ao conectar no banco:
  - Verifique `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
  - Em provedores gerenciados (ex.: Neon), use `sslmode=require`.
  - Certifique que o banco exista e o usuário tenha permissões.

## Licença

Projeto de avaliação técnica.