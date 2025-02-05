FROM openjdk:23-jdk-oracle AS builder

ARG COMPILE_DIR=/compiledir

WORKDIR ${COMPILE_DIR}

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN ./mvnw clean package -Dmaven.test.skip=true

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

# not necessary
# ENTRYPOINT java -jar target/ssf-0.0.1-SNAPSHOT.jar



FROM openjdk:23-jdk-oracle

ARG WORKDIR=/app

WORKDIR ${WORKDIR}

COPY --from=builder /compiledir/target/paf_27w-0.0.1-SNAPSHOT.jar app.jar

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]