# 1. Use OpenJDK base image for building
FROM openjdk:17.0.1-slim AS build

# 2. Set working directory
WORKDIR /app

# 3. Copy Maven wrapper and pom.xml (for dependency resolution)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 4. Download and cache dependencies
RUN ./mvnw dependency:go-offline -B

# 5. Copy source code
COPY src ./src

# 6. Build Spring Boot JAR (use profile if needed, optional)
ARG PROFILE=prod
RUN ./mvnw clean package -DskipTests -P${PROFILE}

# 7. Runtime image (same openjdk JDK image)
FROM openjdk:17.0.1-slim

WORKDIR /app

# 8. Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# 9. Use profile passed in runtime
ARG PROFILE=prod
ENV SPRING_PROFILES_ACTIVE=$PROFILE

# 10. Expose port (optional for docs)
EXPOSE 8081

# 11. Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
