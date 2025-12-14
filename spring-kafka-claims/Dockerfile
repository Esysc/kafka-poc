

# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
# Copy only pom.xml and download dependencies (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline
# Now copy the rest of the source
COPY . .
RUN mvn package -DskipTests

# ---- Run stage ----
FROM eclipse-temurin:25-jdk-jammy
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /build/target/spring-kafka-claims-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
