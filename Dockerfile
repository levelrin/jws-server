FROM openjdk:15
WORKDIR /usr/src/myapp
COPY . /usr/src/myapp
CMD ["./gradlew", "build"]
