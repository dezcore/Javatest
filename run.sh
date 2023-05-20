#!/bin/bash

#javac -cp "libs/*" src/Test.java 
#java -cp .:libs/* src.Test 

#Docker
docker compose down -v
docker build -t javatest .
docker compose up