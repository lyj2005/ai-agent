FROM openjdk:21

WORKDIR /app

# COPY ./user-center-backend-0.0.1-SNAPSHOT.jar ./app/application.jar
COPY ./ai-agent-0.0.1-SNAPSHOT.jar ./app/application.jar

CMD ["java","-jar","./app/application.jar","--spring.profiles.active=prod"]

