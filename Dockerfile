# Use the official OpenJDK image as a parent image
FROM openjdk:21-jdk-slim-buster

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY target/Olik-Assigment-0.0.1-SNAPSHOT.jar olik.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file when the container starts
CMD ["java", "-jar", "olik.jar"]