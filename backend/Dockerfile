# 多阶段构建 - 构建阶段
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# 运行阶段
FROM tomcat:10.1.19-jdk21

# 删除默认应用
RUN rm -rf /usr/local/tomcat/webapps/*

# 复制war包
COPY --from=build /app/target/fruitshop.war /usr/local/tomcat/webapps/ROOT.war

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 暴露端口
EXPOSE 8080

# 启动命令
CMD ["catalina.sh", "run"]
