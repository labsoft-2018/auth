#!/usr/bin/env bash
set -e

NAME="labsoft-2018/auth"


for arg in "$@"
do
    if [[ "$arg" == "build" ]]; then
      echo "Building jar..."
      lein uberjar
      echo "Building docker..."
      docker build -t $NAME .
      echo "Success!"
    elif [[ "$arg" == "run-it" ]]; then
      docker run -it $NAME
    elif [[ "$arg" == "clean" ]]; then
      lein clean
    else
        echo "Choose one of the following options:"
        echo "build -> lein uberjar; docker build"
        echo "clean -> lein clean"
        echo "run-it -> docker run -it"
    fi
done
