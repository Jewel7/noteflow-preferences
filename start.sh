docker compose down
docker compose up -d
#remove the target directory
mvn clean
# compile the source code and package it as a jar into the target folder
mvn package
# make the bucket in MinIO
docker exec -it $(docker container ls | grep 'minio' | awk '{print $1}') mc mb data/preferences
java -jar -Dspring.profiles.active=dev ./target/noteflow-preferences.jar