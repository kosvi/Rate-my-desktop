# first we build the app with maven

FROM maven:3.6.3-openjdk-11-slim as build-stage

WORKDIR /usr/app
COPY . ./
RUN mvn -DskipTests clean package spring-boot:repackage

# ... and then we copy the .jar file to openjdk-image and run it there

# I guess we could have a slimmer image? 
# .. but then again, that's not the first place to start optimizing :)
FROM openjdk:11

WORKDIR /usr/app
COPY --from=build-stage /usr/app/target/rmd-0.0.1-SNAPSHOT.jar /usr/app/

RUN useradd -u 10100 app && \
    chown -R app /usr/app
USER app

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "/usr/app/rmd-0.0.1-SNAPSHOT.jar"]
