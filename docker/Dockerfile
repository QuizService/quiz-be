FROM amazoncorretto:21-alpine-jdk AS builder

WORKDIR /sources

COPY . .

RUN ./gradlew :api-module:build

FROM gcr.io/distroless/java21-debian12

WORKDIR /app

COPY --from=builder /sources/api-module/build/libs/api-module-0.0.1-SNAPSHOT.jar /app/quiz.jar

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "quiz.jar"]
