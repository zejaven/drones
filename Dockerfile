FROM maven:3.6.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*.jar drones.jar
EXPOSE 8084
CMD ["java", "-jar", "drones.jar"]
