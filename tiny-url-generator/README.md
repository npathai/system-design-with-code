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
    - The system has a user `root:root` created, which can be utilized for using authenticated user features 
4) Stop the application stack and react app when done

#### Component Urls
 1) UI Application - `http://locahost:3000`
 2) Nginx loadbalancer - `http://localhost:4000`
 3) Consul - `http://localhost:8500`
 4) Prometheus - `http://localhost:9090`
 5) Grafana - `http://localhost:8081`
