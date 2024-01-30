FROM openjdk:21
EXPOSE 8080
ADD target/Task5-0.0.1-SNAPSHOT.jar Task5.jar
ENTRYPOINT ["java", "-jar", "Task5.jar"]

