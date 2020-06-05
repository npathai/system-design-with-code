# system-design-with-code
System design not just on whiteboard, but with code.

## Why this repository?
This repository represents an effort to learn System Design by experimenting, failing-fast, learning from that experince and providing all the fellow developers a wealth of knowledge in terms of source code.

System design always seemed quiet far from reach because the knowledge available online either is too shallow or basic, or quiet high level. Being a developer, I wanted to learn by looking at code and experimenting. Facing challenges head on and building robust systems. If you feel the same, then this repository is perfect for you!

## Goals
Try to re-create system design case studies, not just on prototype scale but full scale. I intend to deploy sytems on cloud and load test them at scale.

All systems built in this repository should be
 - Microservices driven (or even Serverless)
 - Lightweight
 - Highly scalable
 - Fault tolerant (kill machines randomly and system shouldn't cave in)
 - High performance
 

## URL Shortening Service

Starting the service is a three step process, working towards making it easier to get running in fewer steps.

### How to start

#### Create fat jar

From the root directory

```
gradlew clean jar
```

#### Create docker images

From the root directory

```
# Build the Id generation service
docker build ./id-gen-service -t id-gen-service:latest

# Build the short url generator service
docker build ./short-url-generator -t short-url-generator:latest

```

#### Run docker compose

From the root directory

```
docker-compose run
````
For easy way to view the logs and login to containers use Docker dashboard on Windows OS
