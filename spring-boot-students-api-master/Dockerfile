FROM openjdk:8-jdk-alpine
RUN mkdir /docEnv
VOLUME /docEnv
ADD ./target/rest-mysql-0.0.1-SNAPSHOT.jar /docEnv
ENTRYPOINT ["java", "-jar", "./docEnv/rest-mysql-0.0.1-SNAPSHOT.jar"]