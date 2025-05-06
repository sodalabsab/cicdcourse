## Lab 3 - Setup a local runner
Instead of following the instructions on how to download and setup a local runner on your computer found [here](https://docs.github.com/en/actions/hosting-your-own-runners/managing-self-hosted-runners/adding-self-hosted-runners)

We will take a more platform agnostic path due ot the fact that we already have docker environment up and running. This guide will help you setup and understand the process of deploying a repository specific runner, which means your other repositories does not have access to it.

To begin with we need to create a .env file locally where we store variables, go ahead and rename the `envTemplate` file to `.env` one in the github-runner directory.

Populate the `IMAGE` variable with a name, for example *github-runner:local*, `USERNAME` with your github username and `REPOSITORY` with the current repository name. 

After this we need to visit GitHub UI and set up an Personal Access Token.

1. First navigate to Your profile -> Settings -> Developer settings -> Personal access tokens -> Fine-grained tokens
2. Select Generate Token
3. Give the token a name and description as you see fit, make sure the resource owner is set to your username, incase you are part of an organization
4. Select expiration and what access you want it to have, select *All repositories*
5. In the Repository permissions set Actions and Administration to read & write
6. Click generate token.
7. Copy and Save the token to your .env file for `ACCESS_TOKEN`.

Now view the `Dockerfile`, `docker-compose.yml` and `start.sh` and read the explanations on what is going on in these files.


```bash
# Navigate to the github-runner directory
# Source the .env file
source .env
# For windows users that are not using GitBash, use the following in Powershell:
Get-Content .env | ForEach-Object { $env:$($_.Split('=')[0]) = $_.Split('=')[1] }

# Build the image using multiarch to adhere to ARM64 architecture
# Tag and send it to the artifact repository
docker build -t ${IMAGE} .

# Deploy the image
docker-compose up -d

# And pull down the deployment
docker-compose down
```

Now visit your repository on GitHub.
Enter Settings -> Actions -> Runners

And you should now see your runner with the name "docker-runner-<short-sha>"