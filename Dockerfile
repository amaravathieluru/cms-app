# College CMS
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN cd cms && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/cms/target/*.jar app.jar
EXPOSE 8080
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql.railway.internal:3306/railway
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=TMkTmWIrpNvJioMaTRyfGWjIokQISwNy
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
ENV SERVER_PORT=8080
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SPRING_MAIL_HOST=smtp.gmail.com
ENV SPRING_MAIL_PORT=587
ENV SPRING_MAIL_USERNAME=atieluru@gmail.com
ENV SPRING_MAIL_PASSWORD="rupm jpki shnz afry"
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
ENTRYPOINT ["java", "-jar", "app.jar"]
