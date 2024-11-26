FROM amazoncorretto:21-alpine-jdk AS builder

WORKDIR /sources

COPY . .

RUN chmod u+w ./api-module/src/main/resources && \
    chmod +x gradlew &&  \
    ./gradlew clean &&  \
    ./gradlew :api-module:build

FROM optimoz/openjre-21.0.3:0.4

WORKDIR /app

COPY --from=builder /sources/api-module/build/libs/api-module-0.0.1-SNAPSHOT.jar /app/quiz.jar

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "quiz.jar"]
