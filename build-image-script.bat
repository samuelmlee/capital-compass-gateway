
@echo off
SET version=%1

mkdir build\extracted

call gradlew clean build

java -Djarmode=layertools -jar "build\libs\capitalcompass-gateway-%version%.jar" extract --destination build\extracted

docker build -t "samuelmlee/capitalcompass-gateway:v%version%" .
