# ai-service/Dockerfile
FROM openjdk:17-alpine

# 下载 SkyWalking Agent
ADD https://archive.apache.org/dist/skywalking/9.4.0/apache-skywalking-apm-9.4.0.tar.gz /tmp/
RUN tar -zxvf /tmp/apache-skywalking-apm-9.4.0.tar.gz -C /usr/local/ && \
    mv /usr/local/apache-skywalking-apm-*/agent /skywalking/agent

COPY target/your-app.jar app.jar

# 使用 SkyWalking Agent 启动应用
ENTRYPOINT ["java", "-javaagent:/skywalking/agent/skywalking-agent.jar", "-jar", "app.jar"]