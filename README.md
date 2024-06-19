# Noteflow User Preferences Service README

## Setup

 Start the Docker containers, create the S# bucket, build the app, and run the app with `dev` profile
 
       ./start.sh

This contains the following commands:

    docker compose down
    docker compose up -d
    #remove the target directory
    mvn clean
    #compile the source code and package it as a jar into the target folder
    mvn package
    #make the bucket in MinIO
    docker exec -it $(docker container ls | grep 'minio' | awk '{print $1}') mc mb data/preferences
    java -jar -Dspring.profiles.active=dev ./target/noteflow-preferences.jar

 - open the MinIO console
 - log in with the username `minioadmin` and password `minioadmin`
 - on the left side, click on "Object Browser." You should see one bucket called "user preferences"
 
 ## Testing the Service
 - the Swagger page is on `localhost:8080`
 - posting preferences should populate the bucket, and retrieving them should get the preferences from the bucket

