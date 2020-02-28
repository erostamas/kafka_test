ARG branch
FROM erostamas/producer_build_ubuntu_$branch:latest
MAINTAINER Tamas Eros (tamas.eros1@gmail.com)

# build and install producer
COPY ./ /repos/producer/
WORKDIR /repos/producer
RUN /repos/producer/env/buildenv/ubuntu/build.sh
WORKDIR /repos/producer/build
RUN make install
