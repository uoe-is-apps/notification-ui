# Notification User Interface

This project is the front-end component for the University of Edinburgh's Notification Hub.  It is
developed with Spring Boot 1.4.

## Running this Project

You can run the `notification-ui` project locally (for development) or in a server environment
(TEST, PROD, etc.)

### Running `notification-ms` Locally

#### Database Setup

The Notification Hub uses its own relational database schema.

You can use a Docker-based MS SQL Server instance for local development:

```
$ docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=notification-ui@DEV' -p 1433:1433 microsoft/mssql-server-linux:2017-latest
```

Here are the JDBC connection settings that match the Docker-based MS SQL Server instance shown above.

```
# Notify Database connection settings (MS SQL SERVER example)
datasource.notify.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
datasource.notify.url=jdbc:sqlserver://localhost:1433;
datasource.notify.username=sa
datasource.notify.password=notification-ui@DEV
```

#### Starting the Project

Use the following command to run `notification-ms` locally with the Spring Boot Maven Plugin:

```
$ mvn spring-boot:run
```

[Spring Boot Maven Plugin documentation][]

## Configuration


[Spring Boot Maven Plugin documentation]: https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html#using-boot-running-with-the-maven-plugin
