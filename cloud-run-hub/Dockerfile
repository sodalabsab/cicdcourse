FROM azul/zulu-openjdk:17-latest

# Install curl for health check in docker-compose file for local development
RUN apt-get update && apt-get install -y curl

COPY . /app

WORKDIR /app

RUN chmod a+x ./gradlew && \
    ./gradlew --version && \
    ./gradlew --no-daemon build && \
    mkdir -p /app/build && \
    cp /app/build/libs/hub*.jar /app/build/hub.jar

# Expose port 8080 or port provided by env var
EXPOSE ${PORT:-8080}

# Set the command to run the application when the container starts
CMD ["java", "-jar", "/app/build/hub.jar"]
