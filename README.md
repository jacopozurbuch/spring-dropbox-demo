# Dropbox Demo Project

This project implementes a very basic dropbox-like application, meaning that there are REST Apis for user management and for object storage. 
The aim of this project is to have a learning experience with modern technology frameworks by implementing these to build this simple application.

### Frameworks and Tools

- Spring Boot
- JUnit
- AWS S3, SNS
- Postgres DB
- Gradle

### Application features

### Prerequisites

- Docker
- Java JDK 17

### Initialization Steps

In order to be able to start the application, a database is needed. This project uses postgresql and the fasted way of starting the db server is propably by using the corresponging docker image

```
docker run --env POSTGRES_PASSWORD=${POSTGRES_PASSWD} -p 5432:5432 -d --name db postgres
```

Note: The password needs to be the same as in the application.properties file as the application uses these credetnials to connect to the database.