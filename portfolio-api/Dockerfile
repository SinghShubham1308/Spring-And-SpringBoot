# -------- BUILD STAGE --------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# -------- RUN STAGE --------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/portfolio-api-0.0.1-SNAPSHOT.jar app.jar

# ✅ HARD MEMORY LIMITS (THIS IS THE MAIN FIX)
ENV JAVA_TOOL_OPTIONS="-Xms96m -Xmx256m -XX:MaxMetaspaceSize=96m -XX:ReservedCodeCacheSize=64m -XX:+UseSerialGC"

# ✅ Railway uses dynamic PORT, NEVER hardcode 9090
ENV PORT=8080
EXPOSE 8080

# ✅ JVM WILL NOW ACTUALLY USE MEMORY LIMITS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
