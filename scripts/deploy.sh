#!/bin/bash

# env

# check current running app
cd /home/ec2-user/be
BLUE_APP=$(sudo docker compose -p blue-app -f docker-compose.blue.yml ps | grep Up);

FIND=""
CHECK_URL=""

if [ -z "$BLUE_APP" ]; then
  echo "run blue"
  echo "sudo docker compose -p blue-app -f docker-compose.blue.yml up -d"
  sudo docker compose -p blue-app -f docker-compose.blue.yml up -d
  sleep 20
  for count in {1..5}
  do
    CHECK_BLUE=$(curl -s "$CHECK_URL" | grep blue)
    if [ -z "$CHECK_BLUE" ]; then
      echo "loading... $count"
    else
      echo "start blue success"
      FIND="FIND"
    fi
    sleep 5
  done
  if [ -z "$FIND" ]; then
    echo "start blue failed"
    sudo docker compose -p blue-app -f docker-compose.blue.yml down
  else
    echo "start blue success"
    sudo docker compose -p green-app -f docker-compose.green.yml down
  fi

  else
  echo "run green"
    echo "sudo docker compose -p green-app -f docker-compose.green.yml up -d"
    sudo docker compose -p green-app -f docker-compose.green.yml up -d
    sleep 20
    for count in {1..5}
    do
      CHECK_GREEN=$(curl -s "$CHECK_URL" | grep green)
      if [ -z "$CHECK_GREEN" ]; then
        echo "loading... $count"
      else
        echo "start green success"
        FIND="FIND"
      fi
      sleep 5
    done
    if [ -z "$FIND" ]; then
      echo "start green failed"
      sudo docker compose -p green-app -f docker-compose.green.yml down
    else
      echo "start green success"
      sudo docker compose -p blue-app -f docker-compose.blue.yml down
    fi
fi

sudo rm -rf /home/ec2-user/be