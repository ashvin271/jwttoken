FROM openjdk:11
EXPOSE 9090
ARG JAR_FILE=target/jwt-authentication.jar
ADD ${JAR_FILE} jwt-authentication.jar
ENTRYPOINT ["java","-jar","/jwt-authentication.jar"]