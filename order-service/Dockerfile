# order-service/Dockerfile
FROM openjdk:17-alpine

# 安装 SkyWalking Agent
RUN apk update && apk add --no-cache wget && \
    wget https://archive.apache.org/dist/skywalking/9.5.0/apache-skywalking-apm-9.5.0.tar.gz && \
    tar -xzf apache-skywalking-apm-9.5.0.tar.gz && \
    # 查找并移动 agent 目录
    find . -type d -name "agent" -exec mv {} /skywalking-agent \; && \
    rm -rf apache-skywalking-apm-9.5.0.tar.gz apache-skywalking-apm-*

# 复制应用 JAR 文件
COPY target/order-service-1.0.0-SNAPSHOT.jar app.jar

# 设置环境变量
ENV SW_AGENT_NAME=order-service
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=skywalking-oap:11800
ENV JAVA_OPTS="-javaagent:/skywalking-agent/skywalking-agent.jar"

# 暴露端口
EXPOSE 8086

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]