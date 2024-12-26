# Usar una imagen base de Maven con OpenJDK
FROM openjdk:17-jdk-slim

COPY  /target/foro-covacha-0.0.1-SNAPSHOT.jar /api-v1.jar

ENTRYPOINT ["java", "-jar", "/api-v1.jar"]
