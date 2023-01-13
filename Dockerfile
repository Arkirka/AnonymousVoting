FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
ARG TEMPLATES=documentTemplates/*
COPY ${JAR_FILE} app.jar
RUN mkdir "documentTemplates"
COPY ${TEMPLATES} documentTemplates
ENTRYPOINT ["java","-jar","/app.jar"]