#!/bin/bash

source ~/.bashrc

docker_repo=$DOCKER_REPO
docker_image=$DOCKER_IMAGE
docker_id=$DOCKER_ID
docker_pwd=$DOCKER_PWD
server_name="quiz"

echo "> docker login -u ${docker_id} -p ${docker_pwd}"
docker login -u ${docker_id} -p ${docker_pwd}

echo "> docker pull ${docker_repo}/${docker_image}"
docker pull ${docker_repo}/${docker_image}

echo "> 기존 실행 중인 컨테이너 제거"
echo "> docker rm -f ${server_name}"
sudo docker rm -f ${server_name}

echo "> docker run -d --name ${server_name} --env-file .env -p 8080:8080 --network mongoCluster ${docker_repo}/${docker_image}"
docker run -d --name ${server_name} --env-file .env -p 8080:8080 --network mongoCluster ${docker_repo}/${docker_image}