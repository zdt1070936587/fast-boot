FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
RUN addgroup -S fastboot && adduser -S fastboot -G fastboot
COPY --chown=fastboot:fastboot target/fastboot-boot-monolith-0.1.0-SNAPSHOT.jar app.jar
USER fastboot
EXPOSE 8080 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
