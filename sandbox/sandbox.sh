#!/usr/bin/env bash
# sandbox.sh — launch the AI coding agent sandbox using apple/container
#
# Usage:
#   ./sandbox.sh                        # mount CWD, open interactive shell
#   ./sandbox.sh /path/to/project       # mount a specific folder
#   ./sandbox.sh /path/to/project CMD   # run a single command and exit
#
# Requirements: apple/container installed (https://github.com/apple/container)
#   container system start   # run once after install

set -euo pipefail

IMAGE="ai-agent-sandbox:latest"
PROJECT_DIR="${1:-$(pwd)}"
SHIFT_COUNT=0
[[ $# -ge 1 ]] && SHIFT_COUNT=1
shift "$SHIFT_COUNT" 2>/dev/null || true
CMD=("$@")

# Build the image if it doesn't exist yet (or pass REBUILD=1 to force)
if [[ "${REBUILD:-0}" == "1" ]] || ! container image list 2>/dev/null | grep -q "^${IMAGE}"; then
  echo "Building sandbox image..."
  container build \
    --tag "${IMAGE}" \
    --file "$(dirname "$0")/Dockerfile" \
    "$(dirname "$0")"
fi

# Resolve absolute path
PROJECT_DIR="$(cd "${PROJECT_DIR}" && pwd)"

echo "Mounting: ${PROJECT_DIR} -> /workspace"

BASE_ARGS=(
  --rm
  --interactive
  --tty
  --volume "${PROJECT_DIR}:/workspace"
  # Forward SSH agent so agents can clone private repos
  --ssh
  # Reasonable defaults — tune as needed
  --cpus 4
  --memory 8g
  "${IMAGE}"
)

if [[ ${#CMD[@]} -gt 0 ]]; then
  container run "${BASE_ARGS[@]}" "${CMD[@]}"
else
  container run "${BASE_ARGS[@]}" /bin/bash
fi
