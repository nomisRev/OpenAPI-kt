Dockerfile
Debian Bookworm slim base with:
Category
Search
Filesystem
JVM / Gradle
Build
Language runtimes
Git / SSH
Shell
The WORKDIR is /workspace — that's where the host folder lands.
---
sandbox.sh
Convenience launcher for apple/container:
# Dockerfile for sandboxed AI coding agents
34,797  3% ($0.25)
# Build image (auto-skipped if already built)
./sandbox.sh                          # mount CWD, interactive shell
./sandbox.sh /path/to/project         # mount specific folder
./sandbox.sh /path/to/project CMD     # run one command and exit
REBUILD=1 ./sandbox.sh                # force image rebuild
Key flags passed to container run:
- --volume <host-path>:/workspace — mounts your project
- --ssh — forwards macOS SSH agent (private git repos work)
- --cpus 4 --memory 8g — enough for Gradle builds (tune freely)
- --rm — container is discarded after exit (safe sandbox behaviour)
---
First time setup:
/agents      
/compact     
container system start       # start the apple/container service
./sandbox.sh                 # builds image + opens shell
/connect     
/copy        
/editor      
▣  Build · claude-sonnet-4-6 · 1m 4s
/exit        
Build 