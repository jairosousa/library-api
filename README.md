# library-api [![Build Status](https://travis-ci.org/jairosousa/library-api.svg?branch=master)](https://travis-ci.org/jairosousa/library-api) [![codecov](https://codecov.io/gh/jairosousa/library-api/branch/master/graph/badge.svg)](https://codecov.io/gh/jairosousa/library-api)
Desing API Restull com srping-boot, TDD e novo JUnit5

## Executar os aplicação
```console
mvnw spring-boot:run
```

## Executar os testes
```console
mvnw test
```

## Gerar Builds artefato JAR
```console
mvnw clean package
```

## Gerar Builds artefato WAR
* alterrar a propriedade `<packaging>jar</packaging>` no pom para `<packaging>war</packaging>`

* Adicionar a a dependência do Toncat.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <scope>provider</scope>
</dependency>
```

* Na classe Aplications precisa extender a classe SpringBootServletInitializer

```Java
@SpringBootApplication
public class LibraryApiApplication extends SpringBootServletInitializer {

```
* Execute o comando
```console
mvnw clean package
```
