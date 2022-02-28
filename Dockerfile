FROM adoptopenjdk/openjdk11:alpine
VOLUME /tmp
COPY target/*.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "/app.jar"]
