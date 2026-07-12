## Source: https://github.com/darrendanvers/gradle-docker-example/blob/master/Dockerfile

#
# Gradle image for the build stage.
#
FROM gradle:jdk17 as build-image

#
# Set the working directory.
#
WORKDIR /app

#
# Copy the Gradle config, source code, and static analysis config
# into the build container.
#
COPY --chown=gradle:gradle build.gradle settings.gradle /app/
COPY --chown=gradle:gradle src /app/src

#
# Build the application.
#
RUN gradle --no-daemon build

#
# Java image for the application to run in.
#
FROM eclipse-temurin:17-jdk

#
# Copy the jar file in and name it app.jar.
#
WORKDIR /app
COPY --from=build-image /app/build/libs/poa-manager.jar /app/app.jar

#
# The command to run when the container starts.
#
ENTRYPOINT java -jar /app/app.jar

