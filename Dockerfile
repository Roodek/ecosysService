FROM gradle:jdk17-corretto-al2023 AS build
WORKDIR /app

COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts /app/
COPY gradle /app/gradle
COPY src /app/src

RUN chmod +x gradlew
RUN java -version
RUN ls /app -l
RUN ./gradlew --version
RUN ./gradlew clean build -x test

FROM gradle:jdk17-corretto-al2023
WORKDIR /app


# Copy the Spring Boot jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]