FROM eclipse-temurin:17-jdk-alpine as package
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17.0.4.1_1-jre as builder
WORKDIR /opt/app
COPY --from=package /opt/app/target/*.jar /opt/app/app.jar
RUN java -Djarmode=layertools -jar /opt/app/app.jar extract

FROM eclipse-temurin:17.0.4.1_1-jre
WORKDIR /opt/app
COPY --from=builder /opt/app/dependencies/ ./
COPY --from=builder /opt/app/spring-boot-loader/ ./
COPY --from=builder /opt/app/snapshot-dependencies/ ./
COPY --from=builder /opt/app/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]