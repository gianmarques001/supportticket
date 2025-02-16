# API Support Ticket
É um projeto pessoal cujo tema é o desenvolvimento de uma REST API para gerenciamento de chamados de suporte técnico. Foi construído usando Java, Spring Boot e MySQL.
A finalidade desse projeto é aperfeiçoar conhecimentos e técnicas do framework.


## Instalação

### Pré Requisitos
- Java - Versão 21
- MySQL - Versão 8.0
- Spring Boot - Versão 3.4
- Postman (ou similares) 

### Etapas
1. Baixe o repositório 
2. Descompacte o arquivo .zip 


## Instrução de uso
1. Rode a aplicação utilizando Maven
2. A API estará disponível através de: [http://localhost:8080](http://localhost:8080)


## API Endpoints


```bash
POST /api/v1/auth - Autenticar usuário.

GET /api/v1/persons - Listar todos os usuários do sistema.
GET /api/v1/persons/{idUsuário} - Retorna um usuário em específico pelo ID.

POST /api/v1/clients - Salvar cliente.
PATCH /api/v1/clients/{idCliente} - Atualizar cliente pelo ID.

POST /api/v1/support - Salvar suporte.
GET /api/v1/support - Listar todos os tickets que o suporte aceitou.

GET /api/v1/tickets - Listar todos os tickets que estão em aberto.
GET /api/v1/tickets/{idTicket} - Retorna um ticket pelo ID.

POST /api/v1/tickets - Salvar ticket.
GET /api/v1/tickets/client - Retorna os tickets que foram criados por um cliente.
PATCH /api/v1/tickets/{idTicket} - Atualizar ticket pelo ID.

```

## License

[MIT](https://choosealicense.com/licenses/mit/)
