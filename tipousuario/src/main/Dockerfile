FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8082}"]