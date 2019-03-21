# Notification User Interface

This project is the front-end component for the University of Edinburgh's Notification Hub.  It is
developed with Spring Boot 1.4.

## Running this Project

You can run the `notification-ui` project locally (for development) or in a server environment
(TEST, PROD, etc.)

### Running `notification-ui` Locally

#### Starting the Project

Use the following command to run `notification-ui` locally with the Spring Boot Maven Plugin:

```
$ mvn spring-boot:run
```

[Spring Boot Maven Plugin documentation][]

## Configuration

### Tomcat Settings

The `notification-ui` component uses an embedded Tomcat to present it's self as a web UI.  Enter the tomcat configuration in `application.properties`. 

### Database Connection Settings

The Notification Hub uses its own relational database schema.  As such, the `notification-ui` component requires a database connection.  Enter standard JDBC/JPA connection
settings in `application.properties`.

### Log Settings

The `notification-ui` component logs messages via SLF4J. Enter standard SLF4J configurations in `application.properties`.

## Running a Complete Notification Hub Example
There are a few components to get the 'Notification Hub' up and running.  The follow example is meant as a 'local' deployment.

### Database Setup
Setup a Docker-based MS SQL Server instance for local development:

```
$ docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=notification-ui@DEV' -p 1433:1433 microsoft/mssql-server-linux:2017-latest
```

Configure the Notification Hub to use the Docker-based MS SQL Server instance shown above by configuring `notification-ms/src/main/resources/application.properties` and `notification-ui/src/main/resources/application.properties` with the following JDBC connection settings.

```
# Notify Database connection settings (MS SQL SERVER example)
datasource.notify.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
datasource.notify.url=jdbc:sqlserver://localhost:1433;
datasource.notify.username=sa
datasource.notify.password=notification-ui@DEV
```

### Authentication
CAS has been built into the Notification Hub as a straightforward method to authenticate via SSO.  To run CAS locally, there are a variety of options.  A good way to get CAS up and running quickly is to clone the [uPortal-start][] repo, and run the application per the documentation.  CAS will by default be turned on with uPortal.

Configure the Notification Hub to use CAS via
`notification-ui/src/main/resources/application.properties` .  Locate the `cas.*` properties, and configure them to match your CAS environment.  If running uPortal-start on port 8080 (default settings in uPortal-start), use the following configuration:

```
cas.protocol=http
cas.server=localhost:8080
cas.context=/cas
```

### Web Server Ports
Ensure the following Tomcat ports are configured:

uPortal-start's CAS instance should be set to `8080` (default).

`notification-ms/src/main/resources/application.properties` > `server.port` should be set to `8090`.

`notification-ui/src/main/resources/application.properties` > `server.port` should be set to `8070`.

### Integrations
While most integrations for the Notification Hub are optional, it'd be useful to setup a Twillio account for SMS and Email notifications to ensure a successful end to end test of a local deployment.  Use your Twillio credentials in `notification-ui/src/main/resources/application.properties` to configuration the `uk.ac.ed.notify.sms.twillio.*` properties.

### Launch the Notification Hub
Ensure the following applications are running:

* Docker MS SQL Server
* uPortal-start (for CAS)
* `notification-ms`
* `notification-ui`

Access the Notification Hub UI from http://localhost:8070

[Spring Boot Maven Plugin documentation]: https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html#using-boot-running-with-the-maven-plugin
[uPortal-start]: https://github.com/Jasig/uPortal-start
