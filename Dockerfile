FROM openjdk:17
COPY target/event-gateway-1.0.0.jar /app/event.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=k8s", "/app/event.jar"]
