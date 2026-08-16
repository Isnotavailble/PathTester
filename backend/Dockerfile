# ==========================================
# 1. Build Stage
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Cache Maven dependencies layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build optimized package
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# 2. Runtime Stage (Optimized for Render Free Tier)
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user and group for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy executable jar from build stage
COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar

# Switch to non-root user
USER appuser

# Render dynamically sets PORT (defaults to 8080)
ENV PORT=8080
EXPOSE 8080

# JVM optimizations tailored for Render Free Tier (512 MB RAM & 0.1 CPU):
# - -XX:+UseSerialGC: Disables G1GC in favor of Serial GC, drastically reducing GC memory overhead
# - -XX:MaxRAMPercentage=70.0: Caps max heap at ~358 MB, leaving ample headroom for Metaspace and OS to prevent OOMKilled (Exit 137)
# - -Xss512k: Reduces thread stack memory from 1 MB to 512 KB
# - -Djava.security.egd=file:/dev/./urandom: Eliminates entropy collection delays during cold starts
ENV JAVA_OPTS="-XX:+UseSerialGC -XX:MaxRAMPercentage=70.0 -Xss512k -Djava.security.egd=file:/dev/./urandom"

# Use 'exec' so that termination signals (SIGTERM) from Render are handled directly by the JVM
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar --server.port=${PORT}"]
