FROM gcr.io/google-appengine/openjdk
VOLUME /tmp
ARG JAR_FILE
ADD build/libs/betman-0.1.0.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=production", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]



