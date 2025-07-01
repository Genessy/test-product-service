# --- ÉTAPE 1 : CONSTRUCTION (BUILDER) ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests spring-boot:repackage

# --- ÉTAPE 2 : EXÉCUTION (RUNNER) ---
FROM eclipse-temurin:17-jre AS runner

WORKDIR /app
COPY --from=builder /app/target/msproduct-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]