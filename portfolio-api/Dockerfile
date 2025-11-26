FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Run Stage
FROM openjdk:17.0.1-jdk-slim
# Dhyan dein: Aapke pom.xml mein artifactId 'portfolio-api' aur version '0.0.1-SNAPSHOT' hai
COPY --from=build /target/portfolio-api-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","demo.jar"]