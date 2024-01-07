FROM  --platform=linux/amd64 openjdk:17-jdk-slim
EXPOSE 8080 7463 8888 7464
COPY application/api/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]