#! /bin/sh
FILEPATH=$1
JOB_NAME=$2

IMAGE_NAME=yuhadam/$2

sudo docker build --tag $IMAGE_NAME $FILEPATH

sudo docker login -u yuhadam -p k9460180

sudo docker push yuhadam/$IMAGE_NAME

echo	'{' > $FILEPATH/docker.json
echo 	'"schedule": "R1/2014-09-25T17:22:00Z/PT2M",' >> $FILEPATH/docker.json
echo 	'"name":'	"\"$JOB_NAME\"," >> $FILEPATH/docker.json
echo	'"container": {' >> $FILEPATH/docker.json
echo    '"type": "DOCKER",' >> $FILEPATH/docker.json
echo    '"image":' "\"$IMAGE_NAME\"," >> $FILEPATH/docker.json
echo    '"network": "BRIDGE",' >> $FILEPATH/docker.json
echo	'}' >> $FILEPATH/docker.json
echo  '"cpus": "0.5",' >> $FILEPATH/docker.json
echo  '"mem": "512",' >> $FILEPATH/docker.json
echo  '"uris": [],' >> $FILEPATH/docker.json
echo  '"command": "/tmp/innerSh.sh"' >> $FILEPATH/docker.json
echo '}' >> $FILEPATH/docker.json

sudo curl -L -H Content-Type: application/json -X POST -d @docker.json 192.168.65.121:11701/scheduler/iso8601

sudo curl -L -X PUT chronos-node:8080/scheduler/job/$JOB_NAME
