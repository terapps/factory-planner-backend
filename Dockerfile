FROM openjdk:17-jdk-alpine as build-step

RUN mkdir /app

WORKDIR /app

COPY . /app

RUN ./gradlew clean build

FROM openjdk:17-jdk-alpine as runtime-api

RUN mkdir /app

WORKDIR /app

COPY . --from=/app/api/build/libs/api-*SNAPSHOT.jar

CMD ["java", "-jar", "/app/api*.jar"]

FROM openjdk:17-jdk-alpine as runtime-loader

RUN mkdir /app

WORKDIR /app

COPY . --from=/app/satisfactory-loader/build/libs/satisfactory-loader-*SNAPSHOT.jar

CMD ["java", "-jar", "/app/api*.jar"]
