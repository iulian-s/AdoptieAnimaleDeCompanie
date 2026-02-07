#FROM maven:3.9-eclipse-temurin-21-alpine AS deps
#
#WORKDIR /app
#
#COPY pom.xml .
##download maven dependencies
#RUN mvn dependency:go-offline -B
#
#FROM maven:3.9-eclipse-temurin-21-alpine AS builder
#WORKDIR /app
#COPY --from=deps /root/.m2 /root/.m2
#COPY . .
## Build only the necessary jar and remove unnecessary files immediately
#RUN mvn clean package -DskipTests
#
#
## Runtime
#FROM gcr.io/distroless/java21-debian12:nonroot
#WORKDIR /app
#
## Only copy the SPECIFIC jar file to avoid globbing multiple files
#COPY --from=builder /app/target/*.jar app.jar
#
## Optimization: Ensure 'model' only contains essentials
#COPY --from=builder /app/model ./model
#
#USER nonroot
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
# Create a user for Hugging Face (UID 1000 is required)
RUN useradd -m -u 1000 user
USER user
ENV HOME=/home/user \
    PATH=/home/user/.local/bin:$PATH
WORKDIR $HOME/app

# Copy files from builder
COPY --from=builder --chown=user /app/target/*.jar app.jar
COPY --from=builder --chown=user /app/model ./model
RUN ls -R /home/user/app/model

# Hugging Face uses port 7860 by default
EXPOSE 7860

ENTRYPOINT ["java", "-Xmx4g", "-Xms1g", "-jar", "app.jar", "--server.port=7860"]
