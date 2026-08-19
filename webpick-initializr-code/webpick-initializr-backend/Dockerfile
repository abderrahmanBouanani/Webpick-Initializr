# Stage 1: Build the backend application
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# Copy Maven wrapper and POM descriptor
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Pre-cache Maven dependencies
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B || true

# Copy source code
COPY src src

# Build executable JAR without re-running unit tests
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Minimal runtime image
FROM eclipse-temurin:17-jre-alpine AS runner

WORKDIR /app

# Create non-root system user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy compiled JAR from builder stage
COPY --from=builder /build/target/webpick-initializr-backend.jar app.jar

# Adjust permissions
RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8081

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
