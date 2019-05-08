#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Illegal number of parameters"
    exit
fi

path_docker=$(pwd)
path_project=$(dirname $(pwd))/BREMS
path_jar=$path_project/target
name_image=$1

echo "Compiling java application"
cd $path_project
mvn package -DskipTests

echo "Copying java application"
cd $path_docker
mv $path_jar/SpringDocker.jar .
mv SpringDocker.jar webapp-3.java

echo "Creating docker image"
docker build -t $name_image .