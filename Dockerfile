FROM openjdk:11-jdk

ARG JAR_FILE
CMD ["./gradlew","clean","build","-x","test"]

COPY ${JAR_FILE} app.jar
#COPY target/bloggy-0.0.1-SNAPSHOT.jar backend.jar

#EXPOSE 9092/tcp
ENTRYPOINT ["java","-jar","/app.jar"]
