FROM openjdk:8
WORKDIR /usr/src/myapp
COPY . /usr/src/myapp
CMD ["./gradlew", "build"]
