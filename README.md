# <img src="https://ping-me-resources.s3.us-east-2.amazonaws.com/ping-me-icon-1.png" alt="icon" width="90" height="60"> Ping Me!
Ping Me! is a chat application where users could connect with people they might know and exchange messages and media.
It is a Spring Boot application built using the Spring MVC design pattern.

## Getting Started
### Prerequisites
For building and running the application you need:
1. JDK 1.8
2. Maven 3

### Dependencies
For backend
1. Spring Web
2. Spring Data JPA
3. Spring Security
4. Lombok
5. PostgreSQL Driver
6. Liquibase

For frontend
1. Thymeleaf Templating Engine
2. Bootstrap 5

## How to Run
To run the application locally:
- Clone the repository using the following command:
```
git clone https://github.com/<your-git-username>/ping-me.git
```
- Build the project and run the tests by running:
```
mvn clean package
```
- Finally, run the app by one of these two methods:
```
java -jar -Dspring.profiles.active=test target/ping-me-0.0.1-SNAPSHOT.jar
  
 or
  
mvn spring-boot:run -Drun.arguments="spring.profiles.active=test"
```

## Contributors
- Chijioke Ibekwe


