FROM maven:3.8-jdk-11 AS MAVEN_BUILD
LABEL maintainer="1500418656@qq.com"
WORKDIR /build/
#更换maven国内仓库，以便下载springboot-starter
# COPY settings.xml /root/.m2/
COPY pom.xml .
RUN mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r target/
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true
FROM openjdk:11
WORKDIR /root
EXPOSE 8081
COPY --from=MAVEN_BUILD /build/target/score_assistant-0.0.1-SNAPSHOT.jar /root/score-assistant-be.jar
RUN apt-get update
RUN apt-get install -y python3 python3-pip
RUN mkdir /root/user_data
COPY src/main/resources/get_grades.py /root/get_grades.py
ENTRYPOINT ["java","-jar", "score-assistant-be.jar"]