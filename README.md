# Bank Source Adapters

This project was implemented with [Quarkus](https://quarkus.io/) for its ease of use and development experience, however most of its idioms are similar to the most well spread web frameworks in the Java ecosystem at the moment.

## Project Overview

Bank Source Adapters is a web application that serves bank accounts' data via a HTTP REST API.

It currently has two endpoints:

`/bank/balances/{accountId}`

Returns the all the account balances of a given `accountId` throughout all of the available bank sources.

`/bank/transactions/{accountId}?fromDate=<fromDate>&toDate=<toDate>`

Returns the all the account transactions of a given `accountId` grouped by the bank sources. It also allows a date window filter to be passed as an optional query param through the `fromDate` and `toDate` param.

## Architecture Overview

![Arch Diagram](/img/Diagram.jpg "Arch Diagram")

Callers may call the endpoints via HTTP, those requests are then handled by `BankController` which is responsible for:

- Converting DTOs to HttpResponse object
- Handling exceptions and returning sane state codes for different types of exceptions and unexpected states.

`BankController` then calls `BankService` which aggregates `BankSourceAdapters` and uses them to build either balances or transactions DTO to be consumed by the controller.

`BankSourceAdapter` is an interface that enforces a contract on which each bank source may return a `BankAccount` model object after being passed a `accountId`.

Each `BankSource` may have its own implementation of the `BankSourceAdapter`, meaning that the data source is agnostic for `BankService` or any upstream dependency. The data may be coming from hard-coded values (as this example), a database, or any kind of persistence mechanism. As long as the contract proposed by the `BankSourceAdapter` interface is followed, any new source may be easily added to the codebase with few changes to the overall structure of the application.

## Running the project

### Running the application in dev mode

First install all the dependencies with

```shell script
./mvnw install
```

Then, you may run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

### Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

### Running with docker

You may also run the application using `docker`:

Before building the container image run:

```shell script
./mvnw package
```

Then build the image with:

```shell script
docker build -f src/main/docker/Dockerfile.jvm -t <tag>/<container_name> .
```

Finally you may run the container by running:

```shell script
docker run -i --rm -p 8080:8080 <tag>/<container_name>
```

## Testing the application

You may run the unit tests by running after installing the dependencies

```shell script
./mvnw test
```
