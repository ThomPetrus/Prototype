#!/bin/bash

show_help() {
  printf "\nUsage: %s COMMAND [--OPTION]\n\n" "$0"
  printf "Commands:\n"
  printf "  package               package the application jar file\n"
  printf "  deploy-ecr            deploy docker image to AWS ECR"
  printf "  run                   run the application locally\n"
  printf "  test                  run the tests\n"
}

deploy() {
  mvn clean package
  aws ecr get-login-password | docker login --username AWS --password-stdin 842924693656.dkr.ecr.us-west-2.amazonaws.com
  docker build -t motive .
  docker tag motive:latest 842924693656.dkr.ecr.us-west-2.amazonaws.com/motive/prototype:latest
  docker push 842924693656.dkr.ecr.us-west-2.amazonaws.com/motive/prototype:latest
}

package() {
  mvn clean package
}

run() {
  mvn clean package
  docker-compose up --build
}

test() {
  mvn clean test
}

if [ "$#" -lt 1 ]; then
  show_help
  exit 1
fi

case "$1" in
  setup-hooks)
    setup
  ;;
  run)
    run
  ;;
  package)
    package
  ;;
  deploy-ecr)
    deploy
  ;;
  test)
    test
  ;;
  *)
    printf "\nUnknown option for the command: %s\n" "$1"
    show_help
    exit 2
  ;;
esac

exit 0