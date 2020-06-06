# system-design-with-code
System design not just on whiteboard, but with code.

<a href="https://systemdesignwithcode.slack.com/"><img alt="Slack Status" src="https://badgen.net/badge/icon/slack?icon=slack&label=Join%20discussion"></a>

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

After starting the application stack using `gradlew composeUp`, open `index.html` file which contains not so feature rich UI to use the application.
