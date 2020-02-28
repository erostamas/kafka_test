#/bin/bash

set -e

echo "Building producer build image for ubuntu"

echo "Environment variables:"
env

export WORKSPACE="${WORKSPACE:-$(pwd)}"
SUCCESS=0

cd $WORKSPACE/producer/
BRANCH_NAME=${BRANCH_NAME:-$(git rev-parse --abbrev-ref HEAD)}
ROOT_DIR=$(git rev-parse --show-toplevel)
echo "Branch name is: $BRANCH_NAME"

IMAGE_NAME="erostamas/producer_build_ubuntu_$BRANCH_NAME"
cd $WORKSPACE
docker build -t $IMAGE_NAME -f $WORKSPACE/producer/env/buildenv/ubuntu/Dockerfile $ROOT_DIR
docker login --username=erostamas --password 749af946-ad0c-4d57-ade7-dfcc06efb7e4 docker.io
docker push $IMAGE_NAME:latest

SUCCESS=$?
echo "Done building producer build image for ubuntu"

return $SUCCESS