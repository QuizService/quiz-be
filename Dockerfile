FROM openjdk:21
ARG JAR_FILE=api-module/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]