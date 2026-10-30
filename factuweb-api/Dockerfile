# Se usa una imagen con Maven solo para construir el .jar. Asi no hace
# falta tener Maven ni el JDK completo instalados en la maquina.
FROM maven:3.9-eclipse-temurin-17 AS compilacion
WORKDIR /build

# Primero solo el pom: si no cambian las dependencias, Docker reutiliza
# esta capa y no vuelve a descargarlas en cada construccion.
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Ahora el codigo fuente
COPY src ./src
RUN mvn -B clean package -DskipTests


FROM eclipse-temurin:17-jre
WORKDIR /app

# Usuario sin privilegios: si alguien compromete la aplicacion,
# no tendra permisos de administrador dentro del contenedor.
RUN useradd --system --create-home factuweb

COPY --from=compilacion /build/target/*.jar app.jar

# Carpeta de logotipos y fotos de productos. El volumen se monta en
# docker-compose.yml. En Render el disco es temporal.
RUN mkdir -p /app/archivos-subidos && chown -R factuweb:factuweb /app

USER factuweb
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
