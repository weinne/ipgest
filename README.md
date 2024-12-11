# ipgest

Sistema de Gestão para Igrejas Presbiterianas

## Descrição

O ipgest é um sistema de gestão em desenvolvimento para auxiliar igrejas presbiterianas na administração de seus membros, igrejas e usuários. Ele oferece funcionalidades para cadastro, edição e exclusão de membros e igrejas, além de autenticação e autorização de usuários. O projeto ainda está no seu início, mas você pode colaborar.

## Funcionalidades

- Cadastro de usuários
- Login e logout de usuários
- Cadastro, edição e exclusão de membros
- Cadastro, edição e exclusão de igrejas
- Autenticação e autorização de usuários
- Integração com banco de dados H2 (temporário)

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.0
- Spring Security
- Spring Data JPA
- Thymeleaf
- H2 Database
- Maven

## Estrutura do Projeto

src/ ├── main/ │ ├── java/ │ │ └── br/com/ipgest/ │ │ ├── config/ │ │ ├── constants/ │ │ ├── controller/ │ │ ├── model/ │ │ ├── repository/ │ │ ├── security/ │ │ └── service/ │ └── resources/ │ ├── templates/ │ └── application.properties └── test/ └── java/ └── br/com/ipgest/


## Como Executar

1. Clone o repositório:
    ```sh
    git clone <URL_DO_REPOSITORIO>
    cd ipgest
    ```

2. Compile e execute o projeto usando Maven:
    ```sh
    ./mvnw spring-boot:run
    ```

3. Acesse o sistema no navegador:
    ```
    http://localhost:8080
    ```

## Endpoints

- `/login` - Página de login
- `/home` - Página inicial após login
- `/membros` - Gerenciamento de membros
- `/igreja/registrar` - Cadastro de nova igreja
- `/usuario/registrar` - Cadastro de novo usuário

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

## Licença

Este projeto está licenciado sob a Licença Apache 2.0. Veja o arquivo [LICENSE](http://_vscodecontentref_/2) para mais detalhes.