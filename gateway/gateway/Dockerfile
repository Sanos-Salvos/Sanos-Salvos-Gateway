
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -q


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar


ENV PORT=8080
ENV JWT_SECRET=sanosysalvos1234


ENV BFF_HOST=localhost
ENV BFF_PORT=8083

ENV FRONT_URL=http://localhost:3000

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]