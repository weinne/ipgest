# Ipgest

Ipgest é uma sistema em desenvolvimento para a gestão de igrejas e seus membros.

## Funcionalidades

- **Gerenciamento de Igrejas**: Cadastre e mantenha informações detalhadas sobre as igrejas.
- **Gerenciamento de Membros**: Adicione, edite e visualize informações dos membros da igreja.
- **Gerenciamento de Tarefas**: Crie e acompanhe tarefas e atividades.
- **Gerenciamento de Cultos e Liturgias**: Organize cultos e liturgias, incluindo escalas e liturgias.
- **Relatórios e Gráficos**: Gere relatórios detalhados e gráficos para análise.
- **Suporte a Múltiplos Usuários e Níveis de Acesso**: Controle de acesso baseado em papéis para diferentes usuários.

## Desenvolvimento

Atualize sua conexão de banco de dados local em `application.properties` ou crie seu próprio arquivo `application-local.properties` para substituir
as configurações para desenvolvimento.

Durante o desenvolvimento, é recomendado usar o perfil `local`. No IntelliJ, `-Dspring.profiles.active=local` pode ser
adicionado nas opções de VM da Configuração de Execução após habilitar esta propriedade em "Modificar opções".

O Lombok deve ser suportado pelo seu IDE. Para o IntelliJ, instale o plugin Lombok e habilite o processamento de anotações -
[saiba mais](https://bootify.io/next-steps/spring-boot-with-lombok.html).

Após iniciar a aplicação, ela estará acessível em `localhost:8080`.

## Build

A aplicação pode ser construída usando o seguinte comando:

```sh
mvnw clean package
```

Inicie sua aplicação com o seguinte comando - aqui com o perfil `production`:

```sh
java -Dspring.profiles.active=production -jar ./target/ipgest-0.0.1-SNAPSHOT.jar
```

Se necessário, uma imagem Docker pode ser criada com o plugin Spring Boot. Adicione `SPRING_PROFILES_ACTIVE=production` como
variável de ambiente ao executar o contêiner.

```sh
mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=br.com.ipgest/ipgest
```

## Estrutura do Projeto

A estrutura do projeto é a seguinte:

```
.gitignore
.mvn/
.vscode/
pom.xml
README.md
src/
    main/
        java/
            br/
                com/
                    ipgest/
                        ipgest/
                            controller/
                            domain/
                            model/
                            repos/
                            service/
                            util/
        resources/
            application.properties
            messages.properties
            static/
            templates/
    test/
        java/
target/
```

## Leitura Adicional

* [Documentação do Maven](https://maven.apache.org/guides/index.html)  
* [Referência do Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
* [Referência do Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
* [Documentação do Thymeleaf](https://www.thymeleaf.org/documentation.html)  
* [Documentação do Bootstrap](https://getbootstrap.com/docs/5.3/getting-started/introduction/)  
* [Htmx em poucas palavras](https://htmx.org/docs/)  
* [Aprenda Spring Boot com Thymeleaf](https://www.wimdeblauwe.com/books/taming-thymeleaf/)
