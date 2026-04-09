#!/bin/bash
# Launch Pi Agent in a JVM-enabled sandbox
# Build the image first if it doesn't exist:
#   docker build -f Dockerfile.pi-box -t pi-box .

docker run -it --rm \
  -v "$(pwd)":/app \
  -v "$HOME/.pi":/root/.pi \
  -v "$HOME/.gradle":/root/.gradle \
  -v "$HOME/.m2":/root/.m2 \
  -w /app \
  pi-box:latest
