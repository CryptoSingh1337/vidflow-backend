#!/bin/bash -

echo "############# BUILDING THE JAR #############"
mvn -f vidflow-backend/pom.xml clean package
echo "############# FINISHED #############"

echo "############# COPYING JAR to /var/spring_boot #############"
sudo mkdir /var/spring_boot
sudo cp vidflow-backend/VidFlowWeb/target/VidFlowWeb-1.0.jar /var/spring_boot/app.jar
sudo chmod +x /var/spring_boot/app.jar
echo "############# FINISHED #############"
