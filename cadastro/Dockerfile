FROM openjdk:17-jdk-slim
RUN groupadd -r app && useradd -r -gapp app
RUN mkdir -m 0755 -p ${APP_HOME}/bin
RUN mkdir -m 0755 -p ${APP_HOME}/config
RUN mkdir -m 0755 -p ${APP_HOME}/logs/
ARG APP_HOME=api2
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api.jar
ENTRYPOINT ["java", "-jar", "/api.jar"]