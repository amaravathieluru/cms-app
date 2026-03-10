FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY cms/pom.xml .
COPY cms/src ./src
COPY cms/mvnw .
COPY cms/.mvn ./.mvn
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
