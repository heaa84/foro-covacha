# Usar una imagen base de Maven con OpenJDK
FROM maven:3.8.4-openjdk-17-slim as builder

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el código fuente al contenedor
COPY . /app

# Ejecutar el comando de Maven para construir el proyecto
RUN mvn clean package -DskipTests

# Imagen final con el archivo JAR
FROM openjdk:17-jdk-slim

# Copiar el archivo JAR del contenedor builder
COPY --from=builder /app/target/foro-covacha.jar /app/foro-covacha.jar

# Ejecutar la aplicación
EXPOSE 8080
CMD ["java", "-jar", "/app/foro-covacha.jar"]
