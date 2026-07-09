#!/bin/bash
echo "======================================="
echo "Building Docker image"
echo "======================================="

docker compose -f docker/docker-compose.yml down

docker compose -f docker/docker-compose.yml build --no-cache --progress=plain

docker compose -f docker/docker-compose.yml up