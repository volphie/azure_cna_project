FROM openjdk:8-jdk-alpine
ADD target/sbtemplate-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 9090
ENTRYPOINT ["java","-jar","/app.jar"]