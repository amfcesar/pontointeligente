FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/ponto-inteligente-*.jar

#COPY target/*.jar api.jar

COPY ${JAR_FILE} api.jar

ENTRYPOINT ["java","-jar","/api.jar"]