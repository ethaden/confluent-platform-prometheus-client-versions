# Example for Monitoring Confluent Platform with Prometheus and Grafana based on Docker

# Preconditions

This project has been tested with Java version 17 and Gradle version 8.1.1.

## Running Confluent Platform
Start the containers by running:
````
docker-compose up -d
````

Stopping the containers:
````
docker-compose down
````

Cleaning up (CAREFUL: THIS WILL DELETE ALL UNUSED VOLUMES):
````
docker volumes prune
````

## Building the Producers
Initialize by running
````
gradle wrapper
````

Build Jar including all libraries with:
````
./gradlew shadowJar
````

## Running

````
java -jar producer-7.2.1/build/libs/kafka-producer-7.2.1-0.0.1.jar configuration/dev.properties input.txt
````

