# Use a multi-stage build for efficient image creation

# Stage 1: Build the application
# Use Maven 3.9.5 with Eclipse Temurin JDK 17 for building the application
FROM maven:3.9.5-eclipse-temurin-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to download dependencies
# Copying it separately from the source code allows Docker to cache dependencies
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
# -DskipTests skips running tests to speed up the build process
# Remove this flag if you want to run tests during the build
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
# Use Eclipse Temurin JRE 17 for a smaller runtime image
# 'jammy' refers to Ubuntu 22.04 LTS, which is the base OS for this image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory in the runtime container
WORKDIR /app

# Copy the built JAR file from the build stage
# This step is what makes multi-stage builds powerful - we only copy what we need
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the Spring Boot application
# Note: This is for documentation. You still need to use -p when running the container
EXPOSE 8080

# Command to run the application
# Using array syntax here is preferred for clarity and proper handling of arguments
ENTRYPOINT ["java", "-jar", "app.jar"]

# Additional configuration options (commented out by default):
# To add Java runtime options, you can use:
# ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]

# To add Spring profiles, you can use:
# ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]

# For better performance in a containerized environment, consider adding these JVM flags:
# ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]