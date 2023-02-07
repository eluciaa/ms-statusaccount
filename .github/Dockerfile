FROM maven:3.8.5-openjdk-17-slim AS build
VOLUME /tmp
COPY .. ./
RUN mvn clean package -DskipTests

FROM openjdk:17-oracle
VOLUME /tmp
EXPOSE 8092
COPY --from=build /target/ms-statusaccount-1.0.0.jar ./ms-statusaccount-1.0.0.jar
ENTRYPOINT ["java","-jar","/ms-statusaccount-1.0.0.jar"]