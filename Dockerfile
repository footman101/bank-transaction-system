FROM amazoncorretto:21-alpine AS build
RUN apk add --no-cache maven
WORKDIR /app
COPY pom.xml /app
RUN mvn -f /app/pom.xml dependency:go-offline
COPY src /app/src
RUN mvn -f /app/pom.xml clean package

FROM amazoncorretto:21-alpine
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
EXPOSE 8080