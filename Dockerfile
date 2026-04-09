# Etapa 1: Compilación
FROM eclipse-temurin:21-jdk-jammy as build
WORKDIR /app
COPY . .
# Damos permisos de ejecución al Maven Wrapper y compilamos saltando los tests para agilizar
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copiamos el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Exponemos el puerto que usa Spring Boot por defecto
EXPOSE 8080
# Comando para iniciar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]