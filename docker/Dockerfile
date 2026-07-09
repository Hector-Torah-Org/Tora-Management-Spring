# Build stage
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build --no-daemon


# Runtime stage
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]