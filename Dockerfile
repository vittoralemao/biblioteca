FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:26-jre
WORKDIR /app
RUN groupadd --system spring && useradd --system --gid spring spring
COPY --from=build /app/target/*.jar app.jar
USER spring
ENV JAVA_TOOL_OPTIONS="-Xms128m -Xmx384m"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]