FROM eclipse-temurin:21-jdk-alpine
WORKDIR /home/app
COPY layers/libs /home/app/libs
COPY layers/resources /home/app/resources
COPY layers/app/application.jar /home/app/application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]
