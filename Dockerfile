FROM openjdk:13-alpine
VOLUME /tmp
COPY target/streamer-endpoints-0.0.1-SNAPSHOT.jar target/streamer-endpoints.jar
EXPOSE 8048
ENTRYPOINT ["java","-Dspring.profiles.active=live","-jar","target/streamer-endpoints.jar"]
