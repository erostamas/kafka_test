#/bin/bash

echo "Building producer"

cd $WORKSPACE/producer/
$WORKSPACE/producer/env/buildenv/ubuntu/build_dockerized.sh

echo "Done building producer"
