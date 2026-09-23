# Spring Boot Jenkins Demo

A small Spring Boot REST API designed for learning Jenkins CI/CD and Docker deployment.

## Requirements

- Java 17+
- Maven 3.8+
- Docker
- Jenkins

## Run locally

```bash
mvn clean package
java -jar target/spring-boot-jenkins-demo-1.0.0.jar
```

## Endpoints

- http://localhost:8080/api/hello
- http://localhost:8080/api/info
- http://localhost:8080/actuator/health

## Docker

```bash
mvn clean package -DskipTests
docker build -t spring-boot-jenkins-demo:1.0 .
docker run -d --name spring-boot-demo -p 8080:8080 spring-boot-jenkins-demo:1.0
```

## Jenkins

The included Jenkinsfile demonstrates:

1. Checkout
2. Maven build
3. Unit tests
4. Docker image build
5. Container deployment
6. Health check

For an EC2 deployment, the `Run Container` stage can later be changed to deploy through SSH.
