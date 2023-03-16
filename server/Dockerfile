FROM --platform=arm64v8 maven:3.9.0-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml pom.xml

RUN --mount=type=cache,target=/root/.m2/repository \
  mvn dependency:go-offline

COPY src src 

RUN --mount=type=cache,target=/root/.m2/repository \
  mvn package 

FROM --platform=arm64v8 eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar .

CMD java -jar *.jar
