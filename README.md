[![CircleCI](https://circleci.com/gh/levelrin/jws-server.svg?style=svg)](https://circleci.com/gh/levelrin/jws-server)
[![Test Coverage](https://img.shields.io/codecov/c/github/levelrin/jws-server.svg)](https://codecov.io/github/levelrin/jws-server?branch=main)
[![Maven Central](https://img.shields.io/maven-central/v/com.levelrin/jws-server.svg)](https://maven-badges.herokuapp.com/maven-central/com.levelrin/jws-server)
[![](https://tokei.rs/b1/github/levelrin/jws-server?category=code)](https://github.com/levelrin/jws-server)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/levelrin/jws-server/blob/main/LICENSE)

# jws-server

jws stands for Java WebSocket.
It is a Java library for building a WebSocket server.

## Quick Start

Put the code below on your main method.

```java
new JwsGuide()
    .defaultServerThread()
    // default port is 42069 ;)
    .defaultPort()
    .defaultSocketThread()
    .skipHostValidation()
    .ignorePong()
    .reaction(
        // You should create your class that implements Reaction interface.
        new Reaction() {
            @Override
            public String endpoint() {
                // This reaction belongs to the '/yoi' endpoint.
                return "/yoi";
            }
            
            @Override
            public void onStart(final Session session) {
                // This method will be called
                // when WebSocket communication starts.
            }
            
            @Override
            public void onMessage(final Session session, final String message) {
                // This method will be called
                // when you receive a text message from the client.
                session.sendMessage("A message (reply) to the client.");
            }
            
            @Override
            public void onMessage(final Session session, final byte[] message) {
                // This method will be called
                // when you receive a message from the client in bytes.
            }
            
            @Override
            public void onClose(final Session session, final int code, final String reason) {
                // This method will be called
                // when WebSocket communication is about to be closed
                // for that session.
            }
        }
    ).ready().go();
```

Connect to your server like this (JavaScript):
```javascript
const socket = new WebSocket('ws://localhost:42069/yoi');
```

## Dependency

You just need to add the dependency like so:

Gradle:
```groovy
TBD.
```

Maven:
```xml
TBD.
```

Requirements:
1. JDK 1.15+

## How to contribute?

1. Create a [ticket](https://github.com/levelrin/jws-server/issues).
2. Send a [pull request](https://github.com/levelrin/jws-server/pulls).
Before you do that, run `./gradlew build` and make sure the build is clean.
