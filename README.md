# score-assistant-be

```

FROM maven:3.8-jdk-11 AS MAVEN_BUILD
LABEL maintainer="1500418656@qq.com"
WORKDIR /build/
#COPY pom.xml .
COPY settings.xml /root/.m2/
COPY pom.xml .
RUN mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r target/
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true
FROM openjdk:11
WORKDIR /root
EXPOSE 8080
COPY --from=MAVEN_BUILD /build/target/score_assistant-0.0.1-SNAPSHOT.jar /root/score-assistant-be.jar
ENTRYPOINT ["java","-jar", "score-assistant-be.jar"]

```
