#!/bin/bash

# Generates a unique short-sha from the date and provided info, 
# needed to keep runner names unique in case of multiple runners running on the same host.
generate_hash() {
  echo "${USERNAME}-${REPOSITORY}-$(date +%s.%N)-${RANDOM}" | md5sum | head -c 8
}

# Creates a URL for registering the runner with the provided org/repo.
REG_URL="https://api.github.com/repos/${USERNAME}/${REPOSITORY}/actions/runners/registration-token"

# Creates the name of the runner using the hash method.
RUNNER_NAME="docker-runner-$(generate_hash)"

# Creates a registration token for the runner using the GitHub API with the url created above and the ACCESS_TOKEN provided in the .env.
REG_TOKEN=$(curl -L \
  -X POST \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  "${REG_URL}" | jq .token --raw-output)

# Navigates to the actions runner directory where the runner is installed by the Dockerfile.
cd /home/docker/actions-runner || exit

# Configures and registres the runner with the provided URL, token, 
# and name using the config.sh script file provided from GitHub.
./config.sh --url "https://github.com/${USERNAME}/${REPOSITORY}" --token "${REG_TOKEN}" --name "${RUNNER_NAME}"

# Maintenance script using the same config.sh to unregister the runner.
cleanup() {
    echo "Removing runner..."
    ./config.sh remove --unattended --token "${REG_TOKEN}"
}

trap 'cleanup; exit 130' INT
trap 'cleanup; exit 143' TERM

# Starts the runner using the run.sh script provided from GitHub.
# The wait command is used to keep the script running until the runner is stopped.
./run.sh & wait $!