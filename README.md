# score-assistant-be


```
FROM ubuntu
# 复制应用程序jar包到docker镜像中
COPY target/score_assistant-0.0.1-SNAPSHOT.jar /opt/myapp.jar
# 复制SQL文件到docker镜像中
COPY dump.sql /opt/dump.sql
COPY src/main/resources/get_grades.py /opt/get_grades.py
# 安装MySQL数据库
RUN apt-get update && \
    apt-get install -y mysql-server && \
    apt-get install -y python3 && \
    apt-get install -y openjdk-8-jdk
ENV MYSQL_HOST=localhost MYSQL_PORT=3306 MYSQL_DATABASE=score_assistant MYSQL_USER=root MYSQL_PASSWORD=123456
# 创建user_data文件夹并设置权限
RUN mkdir /opt/user_data && \
    chmod 777 /opt/user_data


# 导入SQL文件到MySQL数据库
RUN service mysql start && \
    mysql < /opt/dump.sql

# 暴露应用程序端口
EXPOSE 8080

# 启动应用程序
CMD ["java", "-jar", "/opt/myapp.jar"]

```
