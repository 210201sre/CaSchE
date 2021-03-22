# docker build -t sentrurion/[image-name]:latest .
# FROM maven:3.6.3-openjdk-8 as BUILDER

# COPY pom.xml pom.xml
# COPY src/ src/

# RUN mvn clean package

FROM openjdk:11
# java:11.0-jre

EXPOSE 8080

ARG JARFILE = target/casche-project-two.jar
ADD ${JARFILE} app.jar
# COPY --from=BUILDER target/casche-project-two.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

