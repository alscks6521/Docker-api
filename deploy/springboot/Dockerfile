FROM openjdk:17.0.2
WORKDIR /app
COPY build/libs/app.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]