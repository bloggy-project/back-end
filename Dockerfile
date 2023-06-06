FROM openjdk:11-jdk

ARG JAR_FILE=/build/libs/bloggy-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

#EXPOSE 9092/tcp
ENTRYPOINT ["java","-jar","/app.jar"]
