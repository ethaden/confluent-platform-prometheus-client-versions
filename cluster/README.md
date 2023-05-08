# 0. Simple Cluster creation plaintext

0. Create a new cluster without securization based
   on [kafka-docker-playground plaintext cluster](https://github.com/vdesabou/kafka-docker-playground/blob/master/environment/plaintext/docker-compose.yml)
1. Disable ksqldb and connect server from default start
2. Create a topic with 2 partitions each partition with 1 replica
3. Create a local producer which produces for the cluster
4. Create a local consumer which consumes from the cluster

## Create the new cluster

Review Create a new cluster without securization based
on [kafka-docker-playground plaintext cluster](https://github.com/vdesabou/kafka-docker-playground/blob/master/environment/plaintext/docker-compose.yml).


## Create a simple maven project

0. Using maven archetype

   mvn archetype:generate \
   -DarchetypeGroupId=org.apache.maven.archetypes \
   -DarchetypeArtifactId=maven-archetype-quickstart

1. Set java version to 11 and add the main class

```xml

   <properties>
       <java.version>11</java.version>
   </properties>
   
   <build>
      <pluginManagement>
          <plugins>
              <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-compiler-plugin</artifactId>
                  <version>3.10.1</version>
                  <configuration>
                      <source>${java.version}</source>
                      <target>${java.version}</target>
                  </configuration>
              </plugin>
              <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-jar-plugin</artifactId>
                  <version>2.4</version>
                  <configuration>
                      <archive>
                          <manifest>
                              <mainClass>io.confluent.csta.examples.security.App</mainClass>
                          </manifest>
                      </archive>
                  </configuration>
              </plugin>
          </plugins>
      </pluginManagement>
   </build>
```

## Solution

Run using

```
   mvn clean package 
   java -jar target/examples-security-1.0.0-SNAPSHOT-jar-with-dependencies.jar config.properties
```