# -------- Build stage --------
FROM gradle:8.7.0-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle clean bootJar -x test

# -------- Run stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh","-c","java -Dserver.port=$PORT -jar /app/app.jar"]
