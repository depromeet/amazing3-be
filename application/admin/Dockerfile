FROM  --platform=linux/amd64 openjdk:17-jdk-slim
EXPOSE 7000
COPY application/admin/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]