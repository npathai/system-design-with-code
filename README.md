# system-design-with-code
System design not just on whiteboard, but with code.

Starting the service is a three step process, working towards making it easier to get running in fewer steps.

## URL Shortening Service

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
