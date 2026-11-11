FROM eclipse-temurin:25-jre
COPY target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app.jar"]
