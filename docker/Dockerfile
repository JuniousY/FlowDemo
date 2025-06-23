# 阶段1：构建阶段
FROM maven:3.9.9-amazoncorretto-21 AS builder

# 设置工作目录
WORKDIR /app
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

# 配置 Maven 国内镜像
COPY docker/settings.xml /root/.m2/settings.xml

# 下载依赖
RUN ./mvnw dependency:go-offline

# 复制源码并构建
COPY src ./src
RUN ./mvnw clean package -DskipTests

# 阶段2：运行阶段
FROM amazoncorretto:21

WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar

# 优化JVM参数（可选）
ENV JAVA_OPTS="-XX:+ExitOnOutOfMemoryError -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp"

# 健康检查端点（需配合Spring Boot Actuator）
#HEALTHCHECK --interval=30s --timeout=3s \
#    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -jar /app/app.jar"]