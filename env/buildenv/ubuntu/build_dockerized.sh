#/bin/bash

ROOT_DIR=$(git rev-parse --show-toplevel)
BRANCH_NAME=${BRANCH_NAME:-$(git rev-parse --abbrev-ref HEAD)}

CMD='cd /workspace/producer && mkdir -p build && rm -rf build/* && cd build && cmake ../ && make -j'

IMAGE_NAME="erostamas/producer_build_ubuntu_$BRANCH_NAME"

echo "Building in image: $IMAGE_NAME"

docker pull "$IMAGE_NAME"
docker run -v $ROOT_DIR:/workspace/producer "$IMAGE_NAME" /bin/bash -c "${CMD}"
