FROM --platform=$BUILDPLATFORM maven:3.9.0-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml pom.xml

RUN --mount=type=cache,target=/root/.m2/repository \
  mvn dependency:go-offline

COPY src src 

RUN --mount=type=cache,target=/root/.m2/repository \
  mvn package \
  && mkdir activmotiv \
  && cd activmotiv \
  && mv /app/target/*.jar . \
  && jar xf *.jar \
  && rm -rf *.jar

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/activmotiv activmotiv

CMD java -cp activmotiv org.springframework.boot.loader.JarLauncher
