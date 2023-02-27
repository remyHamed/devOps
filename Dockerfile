ARG VERSION=19.0.1

FROM gradle:7.6-jdk19 as BUILD

COPY . /src
WORKDIR /src
RUN gradle --no-daemon build

FROM amazoncorretto:${VERSION}

COPY --from=BUILD /src/build/libs/motrelou-2.0-all.jar /bin/runner/run.jar
WORKDIR /bin/runner

CMD ["java","-jar","run.jar"]