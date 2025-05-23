FROM eclipse-temurin:21

RUN mkdir /opt/app
WORKDIR /opt/app

COPY build/libs/labs-0.0.1-SNAPSHOT.jar ./app.jar
COPY src/main/resources/docker.properties ./config/
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "--spring.config.additional-location=config/docker.properties"]
