# Catálogo API

API de catálogo de produtos desenvolvida em Java com Spring Boot, com operações de cadastro, consulta, atualização e remoção, validação de entrada e persistência em PostgreSQL.

O projeto foi construído para consolidar os fundamentos de desenvolvimento Back-End: HTTP, separação de responsabilidades, injeção de dependência, DTOs, tratamento de exceções, persistência e testes automatizados.

## Tecnologias

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Hibernate ORM
- Jakarta Validation
- PostgreSQL
- Maven Wrapper
- JUnit
- Mockito
- MockMvc

## Funcionalidades

- Cadastrar produtos com ID gerado pelo banco.
- Listar produtos.
- Consultar um produto pelo ID.
- Atualizar nome e preço.
- Remover produtos.
- Validar os dados de cadastro e atualização.
- Retornar os erros tratados em JSON.
- Manter os dados após reiniciar a aplicação.

Cada produto possui:

| Campo | Tipo Java | Descrição |
|---|---|---|
| `id` | `Long` | Identificador gerado pelo banco |
| `nome` | `String` | Nome do produto |
| `preco` | `BigDecimal` | Preço do produto |

## Organização

| Classe | Responsabilidade |
|---|---|
| `ProdutoController` | Receber requisições HTTP e preparar respostas |
| `ProdutoService` | Coordenar as operações do catálogo |
| `ProdutoRepository` | Acessar a persistência por meio de `JpaRepository` |
| `Produto` | Representar a entidade persistida |
| `ProdutoRequest` | Representar e validar os dados de entrada |
| `ProdutoResponse` | Representar os dados de saída |
| `ProdutoNaoEncontradoException` | Sinalizar produto inexistente |
| `ApiExceptionHandler` | Transformar as exceções tratadas em respostas HTTP |
| `ErroResponse` | Representar o corpo dos erros tratados |

O projeto também mantém `MensagemController` e `MensagemResponse`, utilizados no primeiro exercício de endpoint com query parameter e resposta JSON.

## Endpoints

URL base: `http://localhost:8080`

| Método | Caminho | Operação | Status de sucesso |
|---|---|---|---|
| GET | `/produtos` | Listar produtos | 200 |
| GET | `/produtos/{id}` | Buscar produto | 200 |
| POST | `/produtos` | Cadastrar produto | 201 |
| PUT | `/produtos/{id}` | Atualizar produto | 200 |
| DELETE | `/produtos/{id}` | Remover produto | 204 |

O endpoint de saudação é:

```http
GET /mensagem?nome=Bruno
```

Quando o parâmetro `nome` não é informado, utiliza o valor `visitante`.

### Cadastro

```http
POST /produtos
Content-Type: application/json
```

```json
{
  "nome": "Monitor",
  "preco": 950.00
}
```

Exemplo de resposta com status 201:

```json
{
  "id": 1,
  "nome": "Monitor",
  "preco": 950.00
}
```

O ID é gerado pelo banco e pode ser diferente do exemplo.

### Atualização

```http
PUT /produtos/1
Content-Type: application/json
```

```json
{
  "nome": "Monitor IPS",
  "preco": 1100.00
}
```

A atualização altera nome e preço, mantendo o identificador. Os dois campos devem ser enviados.

### Remoção

```http
DELETE /produtos/1
```

Uma remoção bem-sucedida retorna 204, sem corpo.

## Validação e erros

Cadastro e atualização exigem:

- Nome informado e com pelo menos um caractere diferente de espaço.
- Preço informado e maior que zero.

As restrições são declaradas em `ProdutoRequest` com `@NotBlank`, `@NotNull` e `@Positive`, e verificadas nos endpoints com `@Valid`.

### Dados inválidos — HTTP 400

```json
{
  "mensagem": "Dados inválidos.",
  "erros": [
    "O nome é obrigatório.",
    "O preço deve ser um valor maior que zero."
  ]
}
```

A ordem das mensagens pode variar.

### Produto inexistente — HTTP 404

```json
{
  "mensagem": "Produto não encontrado.",
  "erros": []
}
```

O formato personalizado cobre erros de validação e produto não encontrado. Outros erros, como JSON malformado, podem utilizar o tratamento padrão do Spring.

## Pré-requisitos

- JDK 25
- PostgreSQL instalado e em execução
- Git para clonar o repositório

Não é necessário instalar Maven separadamente: o projeto inclui Maven Wrapper.

## Configuração do banco

Crie os bancos abaixo no PostgreSQL, pelo pgAdmin ou por outro cliente:

```sql
CREATE DATABASE catalogo;
CREATE DATABASE catalogo_test;
```

- `catalogo`: utilizado pela aplicação.
- `catalogo_test`: utilizado pelo teste de inicialização.

As configurações atuais utilizam `localhost`, porta `5432` e usuário `postgres`. Ajuste os arquivos de configuração se seu ambiente utilizar outros valores.

### Aplicação

Arquivo `src/main/resources/application.properties`:

```properties
spring.application.name=catalogo-api

spring.datasource.url=jdbc:postgresql://localhost:5432/catalogo
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

### Teste de inicialização

Arquivo `src/test/resources/application-test.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/catalogo_test
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

`CatalogoApiApplicationTests` ativa essa configuração com `@ActiveProfiles("test")`.

O Hibernate prepara a estrutura das tabelas com `ddl-auto=update`. Essa configuração foi utilizada no ambiente de estudo; o projeto não implementa migrations.

Não há produtos iniciais inseridos automaticamente. Em um banco novo, a listagem retorna `[]`.

## Executar a aplicação

Clone o projeto:

```powershell
git clone https://github.com/BrunoArial/catalogo-api.git
cd catalogo-api
```

No PowerShell, defina a senha e execute:

```powershell
$env:DB_PASSWORD = 'SUA_SENHA_REAL'
.\mvnw.cmd spring-boot:run
```

Substitua `SUA_SENHA_REAL` pela senha do usuário configurado.

A variável vale para a sessão atual do terminal. É necessário defini-la novamente ao abrir outra sessão.

A aplicação estará disponível em:

```text
http://localhost:8080/produtos
```

No Linux ou macOS, utilize `export DB_PASSWORD='SUA_SENHA_REAL'` e substitua `.\mvnw.cmd` por `./mvnw`.

A senha real não é armazenada nos arquivos de configuração: eles contêm apenas `${DB_PASSWORD}`.

## Testes automatizados

O projeto possui 17 testes:

| Classe | Quantidade | Escopo |
|---|---|---|
| `ProdutoServiceTest` | 6 | Busca, atualização e remoção com repository simulado |
| `ProdutoControllerTest` | 10 | Consultas, cadastro, atualização, remoção, validação e erros HTTP com MockMvc e service simulado |
| `CatalogoApiApplicationTests` | 1 | Inicialização do contexto completo com PostgreSQL |

### Cenários verificados

Os testes do service verificam:

- Busca de produto existente e exceção para produto inexistente.
- Atualização de nome e preço mantendo o ID, com solicitação de salvamento.
- Tentativa de atualizar produto inexistente sem solicitar salvamento.
- Remoção de produto existente com chamada ao repository.
- Tentativa de remover produto inexistente sem solicitar exclusão.

Os testes do controller verificam:

- Consulta por ID com resposta `200` e os dados do produto.
- Consulta de produto inexistente com resposta `404` e JSON de erro.
- Cadastro válido com resposta `201`, dados de saída e conferência dos dados encaminhados ao service.
- Cadastro inválido e cadastro sem preço com resposta `400` e mensagens de validação.
- Atualização válida com resposta `200`, dados de saída e conferência do ID e dos dados encaminhados ao service.
- Atualização inválida com resposta `400` e atualização de produto inexistente com resposta `404`.
- Remoção bem-sucedida com resposta `204`, corpo vazio e chamada ao service com o ID correto.
- Remoção de produto inexistente com resposta `404`, JSON de erro e chamada ao service com o ID correto.

Os testes utilizam recursos como `ArgumentCaptor`, matchers e verificações de interação do Mockito. Para simular uma exceção no método de remoção, que retorna `void`, é utilizado `doThrow`.

### Executar os testes

Os testes do service e do controller não acessam o banco. O teste de inicialização depende do PostgreSQL, do banco `catalogo_test` e de `DB_PASSWORD`.

Para executar somente os 16 testes que não dependem do banco:

```powershell
.\mvnw.cmd "-Dtest=ProdutoServiceTest,ProdutoControllerTest" test
```

Para executar apenas os 10 testes do controller:

```powershell
.\mvnw.cmd "-Dtest=ProdutoControllerTest" test
```

Para executar a suíte completa de 17 testes, mantenha o PostgreSQL em execução e defina `DB_PASSWORD` no mesmo terminal:

```powershell
$env:DB_PASSWORD = 'SUA_SENHA_REAL'
.\mvnw.cmd test
```

Substitua `SUA_SENHA_REAL` pela senha do usuário configurado. A API não precisa estar rodando na porta 8080 para executar os testes.

### Alcance e resultado das verificações

Os testes de service executam sua implementação real com o repository simulado. Os testes do controller exercitam a camada web com o service simulado. Eles verificam o comportamento de cada camada, mas não comprovam alterações reais no PostgreSQL.

O teste de inicialização verifica o carregamento do contexto e a configuração de persistência. Ele não substitui testes automatizados de CRUD no banco.

A permanência de cadastros, atualizações e exclusões após reiniciar a API foi verificada manualmente.

Na execução conjunta mais recente, em 21/10/2026, os 16 testes de service e controller passaram:

```text
Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

A execução completa dos 17 testes e a repetição de `clean package` após a ampliação da suíte ainda estão pendentes.

## Build

Com PostgreSQL em execução e `DB_PASSWORD` definida:

```powershell
.\mvnw.cmd clean package
```

O comando limpa a saída anterior, compila, executa os testes e gera:

```text
target/catalogo-api-0.0.1-SNAPSHOT.jar
```

Para executar o JAR, com a variável definida:

```powershell
java -jar .\target\catalogo-api-0.0.1-SNAPSHOT.jar
```

Encerre uma instância anterior da API antes de iniciar outra na mesma porta. A pasta `target` é saída de build e não deve ser versionada.

## Escopo e conclusão

O projeto foi concluído como uma API educacional de catálogo de produtos, com persistência, validação, DTOs e testes.

Não inclui autenticação, paginação, interface gráfica ou migrations. O uso de `ddl-auto=update` e do usuário `postgres` faz parte da configuração local de estudo.

O escopo funcional está concluído. A suíte foi ampliada com novos cenários da camada web; o estado atual das execuções está documentado na seção de testes automatizados.