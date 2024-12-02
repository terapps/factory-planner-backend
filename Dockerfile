FROM gradle:8-jdk17-alpine AS build-step

RUN mkdir /app

WORKDIR /app

COPY . /app

RUN gradle clean build

FROM openjdk:17-jdk-alpine AS runtime-api

RUN mkdir /app

WORKDIR /app


COPY --from=build-step /app/api/build/libs/api*-SNAPSHOT.jar api.jar


CMD ["java", "-jar", "api.jar"]
