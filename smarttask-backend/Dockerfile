####
# This Dockerfile is used to build a Quarkus application in JVM mode
####
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /build

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

####
# Runtime stage
####
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /build/target/quarkus-app/lib/ /app/lib/
COPY --from=build /build/target/quarkus-app/*.jar /app/
COPY --from=build /build/target/quarkus-app/app/ /app/app/
COPY --from=build /build/target/quarkus-app/quarkus/ /app/quarkus/

EXPOSE 8080

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/quarkus-run.jar" ]
