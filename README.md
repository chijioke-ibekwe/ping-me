# <img src="https://ping-me-resources.s3.us-east-2.amazonaws.com/ping-me-icon-1.png" alt="icon" width="90" height="60"> Ping Me!
Ping Me! is a social media application where users can find and connect with people they know to exchange messages and media. It is a Spring Boot application and features user authentication, real-time messaging/notifications, and user connection preferences.

#### Screenshots:
<img src="https://ping-me-resources.s3.us-east-2.amazonaws.com/screenshots/contacts_page.png" alt="contacts_page" width="700" height="450">
<img src="https://ping-me-resources.s3.us-east-2.amazonaws.com/screenshots/chat_page.png" alt="chat_page" width="700" height="450">
<img src="https://ping-me-resources.s3.us-east-2.amazonaws.com/screenshots/findusers_page.png" alt="find_users_page" width="700" height="450">
<img src="https://ping-me-resources.s3.us-east-2.amazonaws.com/screenshots/request_page.png" alt="connect_requests_page" width="700" height="450">

## Getting Started
### Prerequisites
For building and running the application you need:
1. JDK 1.8
2. Maven 3

### Key Dependencies
For backend
1. Spring Web
2. Spring Data JPA
3. Spring Security
4. Spring WebSocket
5. AWS Java SDK
6. Lombok
7. PostgreSQL Driver
8. Liquibase

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

## Author
- Chijioke Ibekwe (https://github.com/chijioke-ibekwe)


