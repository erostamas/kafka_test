#/bin/bash

ROOT_DIR=$(git rev-parse --show-toplevel)

cd $ROOT_DIR
mkdir -p build
rm -rf build/*
cd build
cmake ../
make -j
