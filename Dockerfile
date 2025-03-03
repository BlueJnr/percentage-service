# Etapa 1: Construcción de la aplicación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final optimizada
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/ROOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
