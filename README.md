# Book Library Manager REST API

This is a  Spring Boot application that provides RESTful endpoints to manage a book library. The application should support CRUD operations for books, authors, and book rentals.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Requirements

For building and running the application you need:

- [Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [Maven 3](https://maven.apache.org/)
- [PostgreSQL (optional)](https://www.postgresql.org/download/)

## Setup

The application has two profiles: dev and local, each with its own set of configurations. The dev profile is used for development and is configured to connect to a deployed database, while the local profile is for local development and uses a local database
- [dev properties](src/main/resources/application-dev.yml)
- [local properties](src/main/resources/application-local.yml)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the [OlikAssigmentApplication](src/main/java/com/midlaj/olikassigment/OlikAssigmentApplication.java) class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

#### Build Application
```shell
mvn clean install
```

#### Run Application

#### dev
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### local
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Running the tests

### Unit  testing

```shell
mvn test
```

### Integration test

```shell
mvn verify -Pintegration-test
```

## Documentation

* [Postman collection](https://www.postman.com/martian-sunset-628462/workspace/olik-assigment/collection/15935546-44af0d6b-a545-46b5-9263-1f682fa763a6?action=share&creator=15935546) - Postman API Collection with documentation
* [Swagger Doc](http://localhost:8080/swagger-ui/index.html) - In-app doc using OpenAPI (after running application)


## Built With

* [Spring Boot](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [CockroachDB](https://www.cockroachlabs.com/) - Database


## Author
* **Muhammed Midlaj** - [miidlaj](https://github.com/miidlaj)
