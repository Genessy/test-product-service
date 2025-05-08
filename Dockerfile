FROM eclipse-temurin:17-jdk-alpine
COPY ./out/artifacts/TestProductService_jar/TestProductService.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]