FROM openjdk:8-jdk-alpine

# Setting Timezone to KST
RUN apk add tzdata && \
    cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

# Install curl for HealthCheck
RUN apk --no-cache add curl

# Extract Server Jar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

HEALTHCHECK --interval=10s --timeout=3s --retries=3 CMD curl -f http://127.0.0.1 || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-server", "-jar", "/app.jar"]
