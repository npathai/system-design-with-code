FROM adoptopenjdk/openjdk11:alpine-slim

# Copy fat jar to docker image
COPY build/libs/short-url-generator.jar /app.jar

# Run the application
CMD ["java", "-jar", "/app.jar"]