
# CI/CD course
This course will guide you through setting up and using a pipeline GitHub Actions and Google Cloud services that serves as a fundation in a CI/CD setup for a demoapplication. Below are detailed instructions for cloning the course code, setting up your local environment, configuring and integrating accounts, and deploying infrastructure and applications.
---

## Setup 1

### Accounts and local development environemt

1. **Microsoft Visual Studio Code (VS Code)**
   - Download and install from: [https://code.visualstudio.com/](https://code.visualstudio.com/)
   - Install helpful extensions: Docker and Git.

2. **Git (if that is not already on your computer)**
   - Download and install from: [https://git-scm.com/](https://git-scm.com/)
   - Configure Git with your name and email:
     ```bash
     git config --global user.name "Your Name"
     git config --global user.email "your.email@example.com"
     ```
   - Verify Git installation:
     ```bash
     git --version
     ```

3. **GitHub Account**
   - Sign up: [https://github.com/join](https://github.com/join)
   - Configure ssh access to GitHub by [following this instruction](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)

4. **Fork the course Repository into your GitHub Account**
   - Go to [the course reporitory](https://github.com/sodalabsab/cicdcourse.git)
   - Select "Fork" to create your own disconnected version of the course code repository
   - Name the repository "cicdcourse" click on "Create fork"

5. **Download the repository localy**
   - Go to the newly created repo in your github account and click on the green "<>Code" button. Copy the SSH URL and open a comand shell on your computer. Paste in this command to create a local repository (connected to the github repository)
     ```bash
     git clone git@github.com:<your-username>/cicdcourse.git
     ```
   - Replace `<your-username>` with your GitHub username.
   - Change directory into the repo:
     ```bash
     cd cicdcourse
     ```
   - Verify the repo with the command
     ```bash
     git remote -v
     ```  
     You sould see something like: `origin	git@github.com:<your usernam>/cicdcourse.git (push)`

6. **Docker (requires local admin)**
   - Donload and install from: [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
   - Ensure Docker is running and verify installation:
     ```bash
     docker info
     ```
    You should see lots of information about the docker environment running on your machine. 
7. **Open the code in VS Code**
   - Start VS Code and open the directory by selecting "Open folder..." from the File meny

### This is the end of the first setup session
Now the course will continue

## Lab 1
Let´s build and setup the system locally.

1. **Make sure Docker i running**
  - Verify that Docker is running locally by 
     ```bash
     docker info
     ```
    This command will show lot´s of information about the docker environment. It there is no ERROR message, then everything is good. 

2. **Build the applications locally**
  - Use the docker-compose.yml file at the root of the repository to build booth applications at once. 
  To start the system use this command:
     ```bash
     docker-compose up --build
     ```  
  This will build booth docker images (using their individual Docker files) and start them in one container each in docker desktop

3. **Verify**
  - Go to http://localhost:8080 and 8081 respectively and verify that booth the client and the hub is running. 

# Setup 2 - move to the cloud

This part explores some of the avaliable tools to build, deploy and monitor applications in Google. There are a few things required to be setup to be able to run the labs. 

1 **Setting up your Google Cloud Environment**

This guide outlines the steps to set up your Google Cloud environment, including creating an account and configuring the Google Cloud CLI.

## 1. Google Cloud Account

- **Sign up for a Google Cloud Free Trial (or Free Tier):** [https://console.cloud.google.com/freetrial](https://console.cloud.google.com/freetrial)

- **Important:** The free trial gives you credits for a limited time.

- **Verify your account:** Google will likely require you to verify your identity and payment information (even for the free tier).

- **Create a Project:** Create a project in the Google Cloud Console. Projects are the way you organize your Google Cloud resources.

## 2. (Optional) Google Cloud CLI (gcloud)

- **Install the gcloud CLI:** [https://cloud.google.com/sdk/docs/install](https://cloud.google.com/sdk/docs/install) Follow the instructions for your operating system.

- **Initialize the gcloud CLI:** After installation, you'll need to initialize it:

  ```bash
  gcloud init
  ```
This will guide you through the process of selecting a project (if you have multiple) and setting up your gcloud configuration.

**Log in to your Google Account:**

  ```bash
  gcloud auth login
  ```
This will open a browser window where you can authenticate with your Google Cloud account.

Set the active project (Important): If you've created multiple projects, you need to tell gcloud which one to use:

  ```bash
  gcloud config set project YOUR_PROJECT_ID
  ```
Replace YOUR_PROJECT_ID with the actual ID of your Google Cloud project. You can find the Project ID in the Google Cloud Console.

## GitHub actions Setup for Google Artifact and Cloud Run Deployment

### Configure GitHub Secrets

You need to configure the following GitHub Secrets in your repository for secure deployment. These secrets are referenced in the actions workflows when pushing docker image and deploying to Google Cloud Run.

#### Steps to Configure Secrets
 

### Setting Up Azure Credentials in GitHub

3. **Store Azure Credentials in GitHub Secrets**
   - Go to your GitHub repository and navigate to **Settings**.
   - Under **Secrets and variables**, click on **Actions**.
   - Click **New repository secret** and add a secret named `GCP??`.
   - Past in the JSON ouput from step 2 so that `GCP??` secret is a JSON string containing your credentials.

---

### How to Use

1. **Trigger the Workflow Manually**:
   - Navigate to the **Actions** tab in your GitHub repository.
   - Select the **Lab Bicep Deployment** workflow.
   - Click on **Run workflow** and specify the `labPath` input (e.g., `lab3`, `lab4`, or `lab5`) to choose which lab's Bicep file to deploy.


---

### Notes

- **Resource Group Naming**: The resource group name is constructed dynamically, combining a base name with the lab number. This helps in organizing resources by lab.
- **Incremental Deployment**: The `--mode Incremental` flag ensures that only changes are applied, preventing the deletion of existing resources.

This workflow provides a structured and automated way to manage Azure deployments for multiple labs, making it easy to set up and scale cloud infrastructure.
## Deploying the Test WebApp

You can deploy the test web application by:

1. **Pushing a Change**: Any change in the repository (e.g., code or configuration updates) will automatically trigger the deployment workflow.
2. **Manually Triggering**: Use the **`webapp-workflow`** in GitHub Actions to manually deploy the web app.

This will build and deploy the web application to your Google clooud run environment.


### Build and Test

The application itself is built with Spring Boot 3, Java 17, and can be compiled, tested and started
locally with the included Gradle wrapper:

  ```
  ./gradlew build test
  java -jar build/libs/demo-0.0.3-SNAPSHOT.jar # or whatever version we're at
  ```

Alternatively, you can build and run it in a container:

  ```
  docker build -t demo .
  docker run --rm -p 8080:8080 demo
  # make sure it's alive
  curl localhost:8080
  ```

Once running, you will find ways to interact with the application at http://localhost:8080/.

</details>

----

<details style="background-color: #303030">
<summary><span style="font-size: 1.8em; font-weight: bold">1️⃣ First Exercise</span></summary>

### Create a Fork

Start by creating a fork of this repository under your own GitHub account. From there you can
experiment freely - even deploy it to our cloud (once we give you the necessary credentials)!

![image](docs/img/0_00_create-new-fork.png)

----

Select your own GitHub account as the "Owner", and click the "Create fork" button.

![image](docs/img/0_01_fork-owner.png)

### Add GCP Credentials

In order to push and subsequently deploy the containerized service, we need to add the appropriate
Google Cloud credentials. Start by going into the project settings:

![image](docs/img/1_00_project-settings.png)

----

In the left-hand menu, under "Security", find "Secrets and variables". Click it, and then "Actions"
below:

![image](docs/img/1_01_actions-secrets-and-variables.png)

----

Here, add a "New repository secret":

![image](docs/img/1_02_new-repository-secret.png)

----

The `name` must be `GCP_CREDENTIALS`.

The `secret` will be provided by today's instructor. Copy and paste that json into the text field
and hit "Add secret":

![image](docs/img/1_03_add-secret.png)

### Enable GitHub Actions Workflow

The definition of the pipeline for this project is included as code in the project itself - GitHub
reads it from the `.github/workflows/` folder. However, it is not enabled by default. To enable it,
click "Actions" in the top project menu:

![image](docs/img/2_00_actions-menu.png)

Here, you will also be informed that workflows are disabled by default. Go ahead and enable them,
and we will continue the exercise by looking at the pipeline and its steps in more detail.

![image](docs/img/2_01_enable-workflows.png)

----

Lastly, to verify that everything is set up and configured correctly, let's run the "Initial Cloud
Run Deploy" action:

![image](docs/img/3_00_verify-pipeline.png)

----

![image](docs/img/3_01_run-workflow.png)

----

Within seconds, the running workflow should appear:

![image](docs/img/3_02_initial-cloud-run-deploy-workflow.png)

Click it, and you will see the workflow details - including the status of the individual steps:

![image](docs/img/3_03_workflow-details.png)

Once all steps are green, we're ready to move on!

This pipeline is meant to demonstrate a "basic" or "typical" setup where the service is built,
unit-tested, containerized, pushed to an artifact registry and then deployed to a staging
environment. End-to-end tests are then run and, if successful, the service is deployed to
production.

The `deploy-production` job has an `output` section which gives you the publicly accessible URL of
the deployed service.

![image](docs/img/4_get-production-deploy-output.png)

</details>

❗ Create a fork of this repository under your own GitHub account  
❗ Deploy your service to our cloud, find and navigate to the service in your browser, and leave some
feedback for the first exercise

----

<details style="background-color: #303030">
<summary><span style="font-size: 1.8em; font-weight: bold">2️⃣ Second Exercise</span></summary>

### Teamwork

As we have seen, this service is part of a bigger whole. It talks to a "hub" service where you can
use it to leave feedback for the completed exercises. So far, the hub has been out of your control -
but now we're going to start collaborating more and adding functionality to both services in order
to increase the value for the end-users.

In this exercise, we will add some functionality to both services - allowing additional information
to be sent to the hub, and visualized on the hub dashboard.

The hub only accepts a number as feedback for an exercise, a "score" or "grade" if you will. And it
interprets it as "score for the first exercise". Or, the only exercise. But as we have now reached
exercise number two, perhaps you are starting to see the dilemma. We will have to update the API of
the hub to accept (and handle) more information - but we must do it in a controlled and structured
way so that we don't break the service for clients that haven't been updated yet.

</details>

❗ Create a PR for the "exercise 2" branch to main    
❗ Sync rollout with the instructor, making sure the backend hub is upgraded first  
❗ After the hub is upgraded, double-check that your service still works before deploying a new
version  
❗ Merge the "exercise 2" PR, wait for it to deploy, and leave some feedback for the second exercise!  

