# Build stage
<#if buildTool?? && buildTool?lower_case == "gradle">
FROM gradle:8-jdk${javaVersion} AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build -x test
<#else>
FROM maven:3.8.5-openjdk-${javaVersion} AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
</#if>

# Run stage
FROM openjdk:${javaVersion}-jdk-slim
WORKDIR /app
<#if buildTool?? && buildTool?lower_case == "gradle">
COPY --from=build /app/build/libs/*.jar app.jar
<#else>
COPY --from=build /app/target/*.jar app.jar
</#if>
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
