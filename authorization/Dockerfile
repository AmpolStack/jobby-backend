# Etapa de compilación
FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /app
# Copiamos los archivos POM: padre, shared y employee
COPY pom.xml .
COPY shared/pom.xml shared/pom.xml
COPY employee/pom.xml employee/pom.xml
COPY authorization/pom.xml authorization/pom.xml
COPY gateway/pom.xml gateway/pom.xml
# Copiamos el código fuente de shared y employee
COPY shared/src shared/src
COPY employee/src employee/src
COPY authorization/src authorization/src
COPY gateway/src gateway/src
# Construimos el JAR sin ejecutar tests
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17-slim
WORKDIR /app
# Crear usuario de sistema para ejecutar la aplicación
RUN adduser --system --group app-user
# Copiamos el JAR compilado desde la etapa build
COPY --from=build /app/authorization/target/*-SNAPSHOT.jar app.jar
# Exponemos el puerto por defecto de Spring Boot (8080)
EXPOSE 8080
USER app-user
# Comando de inicio
CMD ["java","-jar","app.jar"]
