#!/bin/bash

# env

# check current running app
cd /home/ec2-user/be
BLUE_APP=$(sudo docker compose -p blue-app -f docker-compose.blue.yml ps | grep Up);

FIND=""
CHECK_BLUE_URL="http://localhost:8080/health-check"
CHECK_GREEN_URL="http://localhost:8081/health-check"

if [ -z "$BLUE_APP" ]; then
  echo "run blue"
  echo "sudo docker compose -p blue-app -f docker-compose.blue.yml up -d"
  sudo docker compose -p blue-app -f docker-compose.blue.yml up -d
  sleep 20
  for count in {1..5}
  do
    CHECK_BLUE=$(curl -s "$CHECK_BLUE_URL" | grep blue)
    UP_COUNT=$(echo ${CHECK_BLUE} | grep 'UP' | wc -l)

    if [ ${UP_COUNT} -ge 1 ] then;
      echo "start blue success"
      FIND="FIND"
    else
      echo "loading... $count"
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
      CHECK_GREEN=$(curl -s "$CHECK_GREEN_URL" | grep green)
      UP_COUNT=$(echo ${CHECK_GREEN} | grep 'UP' | wc -l)
      if [ ${UP_COUNT} -ge 1 ] then;
        echo "start green success"
        FIND="FIND"
      else
        echo "loading... $count"
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