# 使用包含SkyWalking Agent的官方镜像
FROM apache/skywalking-java-agent:9.4.0-java17 as agent

# 然后使用Java镜像
FROM openjdk:17-alpine

# 设置工作目录
WORKDIR /app

ARG APP_PORT=8083

# 从agent阶段复制SkyWalking Agent
COPY --from=agent /skywalking/agent /app/skywalking-agent

# 复制应用程序
COPY target/email-service-1.0.0-SNAPSHOT.jar app.jar

# 设置环境变量
ENV SW_AGENT_NAME=email-service
ENV APP_PORT=${APP_PORT}
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICE=host.docker.internal:11800
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# 暴露应用端口
EXPOSE ${APP_PORT}

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS \
    -javaagent:/app/skywalking-agent/skywalking-agent.jar \
    -Dskywalking.agent.service_name=${SW_AGENT_NAME} \
    -Dskywalking.collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICE} \
    -Dserver.port=${APP_PORT} \
    -jar app.jar"]

