FROM openjdk:21
ARG JAR_FILE=./deploy/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]