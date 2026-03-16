# AI Coding Agent Sandbox
# Compatible with apple/container (OCI) and standard Docker
# Designed for Apple Silicon (arm64); also builds for amd64 via Rosetta
FROM debian:bookworm-slim

# --- System packages ---
RUN apt-get update && apt-get install -y --no-install-recommends \
    # Core utilities
    bash \
    curl \
    wget \
    git \
    ca-certificates \
    gnupg \
    unzip \
    zip \
    tar \
    # Search / filesystem tools agents rely on
    ripgrep \
    grep \
    findutils \
    fd-find \
    tree \
    jq \
    # Build essentials (gradlew needs these)
    make \
    gcc \
    g++ \
    # JVM (Gradle / Kotlin projects)
    default-jdk \
    # Node.js (many agent toolchains need it)
    nodejs \
    npm \
    # Python (common agent dependency)
    python3 \
    python3-pip \
    # Network tools
    openssh-client \
    dnsutils \
    iputils-ping \
    # Misc
    procps \
    less \
    vim \
    && rm -rf /var/lib/apt/lists/*

# Symlink fd-find -> fd (Debian ships it as fd-find)
RUN ln -sf /usr/bin/fdfind /usr/local/bin/fd

# --- Glob binary (standalone, for agents that call `glob` directly) ---
# Install via npm globally so `glob` is on PATH
RUN npm install -g glob-cli

# --- Gradle wrapper support ---
# Pre-populate the Gradle cache directory so gradlew can write there
ENV GRADLE_USER_HOME=/root/.gradle
RUN mkdir -p "${GRADLE_USER_HOME}"

# --- Java / JVM tuning ---
ENV JAVA_OPTS="-Xmx4g -Xms512m"

# --- Working directory ---
# The host project folder is expected to be mounted here
WORKDIR /workspace

# --- Default entrypoint ---
# Drop into bash so agents can run arbitrary commands.
# Pass a command via `container run ... <cmd>` to override.
CMD ["/bin/bash"]
