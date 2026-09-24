#!/usr/bin/env bash

set -e

if [[ ! -f .env ]]; then
    echo "Error: .env not found"
    exit 1
fi

set -a
source .env
set +a

./gradlew bootRun