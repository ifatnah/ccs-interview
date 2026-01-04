# Base image with Java 21
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apk add --no-cache maven

# Build the project
RUN mvn compile

# Expose the game port
EXPOSE 8080

# Run the server
CMD ["java", "-cp", "target/classes", "server.GameServer"]