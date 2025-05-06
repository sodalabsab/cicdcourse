
FROM debian:bookworm-slim

# This first line is required to support multi-arch builds
ARG TARGETPLATFORM

# Here we define the version of the GitHub Actions Runner.
# The version is set to 2.321.0 but it will update to the latest minor.
ARG RUNNER_VERSION="2.321.0"
ARG DEBIAN_FRONTEND=noninteractive

# We add a docker user to the environment
RUN useradd -m docker

# We use debian package manager to install required packages
# We also install some nice to have packages like curl, jq, git, etc.
RUN apt-get update -y && \
    apt-get install -y --no-install-recommends \
    curl=7.88.1-10+deb12u12 \
    ca-certificates=20230311 \
    jq=1.6-2.1 \
    dos2unix=7.4.3-1 \
    git=1:2.39.5-0+deb12u2 \
    libssl-dev=3.0.15-1~deb12u1 \
    libffi-dev=3.4.4-1 \
    python3=3.11.2-1+b1 \
    python3-venv=3.11.2-1+b1 \
    python3-pip=23.0.1+dfsg-1 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# We set the working directory to /home/docker/actions-runner
WORKDIR /home/docker/actions-runner

# We fetch the latest version of the GitHub Actions Runner and install it
# We also install the dependencies required to run the runner
# We set the ownership to the docker user and group
# Finally we remove the tarball to save space on the image.
RUN curl -O -L https://github.com/actions/runner/releases/download/v${RUNNER_VERSION}/actions-runner-linux-${TARGETPLATFORM}-${RUNNER_VERSION}.tar.gz && \
    tar xzf ./actions-runner-linux-${TARGETPLATFORM}-${RUNNER_VERSION}.tar.gz && \
    rm -f ./actions-runner-linux-${TARGETPLATFORM}-${RUNNER_VERSION}.tar.gz && \
    chown -R docker ~docker /home/docker && \
    /home/docker/actions-runner/bin/installdependencies.sh

# We copy the start.sh script to the image and set the ownership to the docker user and group
COPY --chown=docker:docker start.sh /home/docker/start.sh

# We set the permissions to execute the script and convert it to unix format
# We run dos2unix to convert the script to unix format to avoid issues with line endings
RUN chmod +x /home/docker/start.sh && \
    dos2unix /home/docker/start.sh

# We switch to the docker user so the user does not have access to the root
# We also set the working directory to /home/docker
USER docker
WORKDIR /home/docker

# We set the entrypoint to the start.sh script
ENTRYPOINT ["./start.sh"]