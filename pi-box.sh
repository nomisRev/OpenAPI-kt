#!/bin/bash
# Launch Pi Agent in a JVM-enabled sandbox
# Build the image first if it doesn't exist:
#   docker build -f Dockerfile.pi-box -t pi-box .

docker run -it --rm \
  -v "$(pwd)":/app \
  -v "$HOME/.pi":/root/.pi \
  -v "$(dirname "$0")/jetbrainsai-proxies.js":/root/.pi/agent/extensions/jetbrainsai-proxies.js:ro \
  -v "$HOME/.gradle":/root/.gradle \
  -v "$HOME/.m2":/root/.m2 \
  -w /app \
  pi-box
