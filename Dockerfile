FROM eclipse-temurin:17-jre-alpine
COPY /target/lotto.jar /lotto.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/lotto.jar"]
s