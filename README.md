# system-design-with-code :computer:
System design not just on whiteboard, but with code.

[![Build Status](https://api.travis-ci.org/npathai/system-design-with-code.svg?branch=master)](https://travis-ci.org/npathai/system-design-with-code)


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
    - No single point of failure
 - High performance
 - Observable and Traceable
 

## URL Shortening Service

Starting the service is a single step process.

### How to start/stop the application stack

```
# Start the application stack
gradlew composeUp

# Stop the application stack
gradlew composeDown
````
For easy way to view the logs and login to containers use Docker dashboard on Windows OS

### Start Shortening the URLs

1) Start the application stack using `gradlew composeUp`
2) Switch to `app` and execute the `npm install` followed by `npm start`, this will start the not so feature rich React app
    - Working on creating image for this as well, so can be part of docker compose stack
3) Shorten the URLs at `http://localhost:3000` :scissors:
4) Stop the application stack and react app when done

### Technology Stack

1) Micronaut Framework
2) Zookeeper
3) Consul Service Discovery
4) MySQL
5) Redis Cache
6) Test Containers 
7) React (frontend)
8) Docker
