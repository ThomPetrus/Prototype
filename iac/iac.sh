#!/bin/bash

show_help() {
  printf "\nUsage: %s COMMAND [--OPTION]\n\n" "$0"
  printf "Commands:\n"
  printf "  deploy              deploy CDK app\n"
}

#todo figure out multiple environments
deploy() {
  acc_num=$(aws sts get-caller-identity)
  export ENV="dev"
  cdk deploy -c accountId="$acc_num" -c env="dev" --all
}

if [ "$#" -lt 1 ]; then
  show_help
  exit 1
fi

case "$1" in
  deploy)
    deploy
  ;;
  *)
    printf "\nUnknown option for the command: %s\n" "$1"
    show_help
    exit 2
  ;;
esac

exit 0