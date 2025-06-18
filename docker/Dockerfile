# 阶段1：构建阶段
FROM amazoncorretto:21 as builder

# 设置工作目录
WORKDIR /app

# 利用缓存层加速构建（先复制 pom.xml 和依赖）
COPY pom.xml .
COPY .mvn .mvn
# 下载依赖（此层可缓存）
RUN ./.mvn/mvnw dependency:go-offline

# 复制源码并构建
COPY src src
RUN ./.mvn/mvnw clean package -DskipTests

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