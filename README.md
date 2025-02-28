
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
  
    Start by creating a fork of this repository under your own GitHub account. From there you can
experiment freely
   - Go to [the course reporitory](https://github.com/sodalabsab/cicdcourse.git)
   - Select "Fork" to create your own disconnected version of the course code repository
   - Marke sure to uncheck "Copy the main branch only" there is a branch in there we will use later
   - Name the repository "cicdcourse" click on "Create fork"

5. **Download the repository locally**
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
  In orders to setup a local development environemt - we are using docker desktop as execution plattform. If you are not able to install docker because of local admin rights, you can still take part of lab 2-4. 
   - Donload and install from: [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
   - Ensure Docker is running and verify installation:
     ```bash
     docker info
     ```
    You should see lots of information about the docker environment running on your machine. 
7. **Open the code in VS Code**
  
    VS code is a great universal IDE with many plugins that helps develop applications efficiently. For this course you will only need the bare minimum, but we encurage to explore and learn as much as possible. Most of the things in booth GitHub and Google cloud can be done directly from the IDE.

   - Start VS Code and open the directory by selecting "Open folder..." from the File meny

### This is the end of the first setup session
Now the course will continue with some more slides

## Lab 1
Let´s build and setup the system locally.

1. **Make sure Docker i running**
  - Verify that Docker is running locally by 
     ```bash
     docker info
     ```
    This command will show lot´s of information about the docker environment. It there is no ERROR message, then everything is good. If there is, fix it

2.  **Build and Test locally**
Booth the demo application and the hub are built with Spring Boot 3, Java 17, and can be compiled, tested and started locally with the included Gradle wrapper:

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

3. **Build the system locally**
There is a way to build and start booth components locally in a dev environment using Docker desktop.

  - Use the docker-compose.yml file at the root of the repository to build booth applications at once. 
  To start the system use this command:
     ```bash
     docker-compose up --build
     ```  
  This will build booth docker images (using their individual Docker files) and start them in one container each in docker desktop. It will setup a small network (cloud-run-network) so the applications can talk to eachother using its service names. 

3. **Verify**
  - Go to http://localhost:8080 and http://localhost:8081 respectively and verify that booth the client and the hub is running. 

# Setup 2 - move to the cloud

This part explores some of the avaliable tools to build, deploy and monitor applications in Google. There are a few things required to be setup to be able to run the labs. 

1 **Setting up your Google Cloud Environment**

This guide outlines the steps to set up your Google Cloud environment, including creating an account and configuring the Google Cloud CLI.

## 1. Google Cloud Account

- **Sign up for a Google Cloud Free Trial (or Free Tier):** [https://console.cloud.google.com/freetrial](https://console.cloud.google.com/freetrial)

- **Important:** The free trial gives you credits for a limited time.

- **Verify your account:** Google will likely require you to verify your identity and payment information (even for the free tier).

- **Create a Project:** Create a project in the Google Cloud Console. Projects are the way you organize your Google Cloud resources.

- **Create an Artifact repository :** Go to the dashboard and seach for "Artifact repository". Create one and call it "demo". This step requires a billing account that will ask for credit card details. It will be for free and after the course you can remove the details.

- **Create a service account :** This will allow GitHub action to manage the deployment in GCP. Go to IAM. Create a "Service account" and give it a name - for instance "github-actions".
The service account need to have several rights, including: 
- Artifact Registry Create-on-Push Writer
- Artifact Registry Writer
- Cloud Run Admin
- Cloud Run Service Agent
- Service Account Token Creator

  Once it is created, open it and create an access key. Select "Keys" in the top menue and "Add key". Create a new Json key and download the generated file containing the key.

## 2. (Optional) Google Cloud CLI (gcloud)

- **Install the gcloud CLI:** [https://cloud.google.com/sdk/docs/install](https://cloud.google.com/sdk/docs/install) Follow the instructions for your operating system.

- **Initialize the gcloud CLI:** After installation, you'll need to initialize it:

  ```bash
  gcloud init
  ```
  This will guide you through the process of selecting a project (if you have multiple) and setting up your gcloud configuration.

- **Log in to your Google Account:**

  ```bash
  gcloud auth login
  ```
  This will open a browser window where you can authenticate with your Google Cloud account.

  Set the active project (Important): If you've created multiple projects, you need to tell gcloud which one to use:

  ```bash
  gcloud config set project YOUR_PROJECT_ID
  ```
  Replace YOUR_PROJECT_ID with the actual ID of your Google Cloud project. You can find the Project ID in the Google Cloud Console.

## 3. GitHub actions Setup for Google Artifact and Cloud Run Deployment

### Configure GitHub Secrets

You need to configure the following GitHub Secrets in your repository for secure deployment. These secrets are referenced in the actions workflows when pushing docker image and deploying to Google Cloud Run.

#### Steps to Configure Secrets
Go to your GitHub account and the repository you have cloned from the course repo.
- Select settings->Secret and variables->Actions. 
- Add a new repository secret called GCP_CREDENTIALS and paste the contect of the key-file intor the "Secret" field. (The key-file is the one you downloaded from google cloud when you created the service account above)
- Create a variable (it is in another tab than secrets) called PROJECT_ID and as value, find the ID of the GCP project. Ex: "cicdcourse-32242
You will find the ID in GCP if you click on the projekt box at the top of the GCP dashboard. 

That´s it, now you are ready for Lab 2.

---

## Lab 2 - Deploy to production
Now we are finally ready to start the pipeline and deploy to production. Here we only focus on the Demo client application. The Hub has been deployed centrally (one central hub for the entire class at adress: https://ci-cd-course-hub-aeyvkm6j4q-lz.a.run.app/)

- Go to your GitHub repository and click on "Actions". Enable GitHub actions. This will scan the repository and add the workflow definitions under .github/workflows <- This is default in GitHub actions
- Select "Build, test and deploy Demo application" and click on "Run workflow" to trigger a pipeline run manually.
Follow the progress by clicking on the newly started pipeline instance and expand the logs.
If there are any errors the pipeline will stop. Otherwise, this will result in two instances of the demo applation running in your GCP environment, one for stage (test) and one production.
- In the pipeline find the “Show Output” step
this reveals the URL for your personal dashboard - go to it and register your client with the central Hub. 
- Go to GCP dashboard and look at the logs of the production application to moke sure it has successfully registered with the hub. 

That´s it for lab 2! 

## Lab 3 - Setup a local runner
Follow the instructions on this page to download and setup a local runner on your comupter: 
https://docs.github.com/en/actions/hosting-your-own-runners/managing-self-hosted-runners/adding-self-hosted-runners

- Open the workflow definition for the demo application ./github/workflows/demo-application.yml 
You can do it locally in VS Code or directly in the GitHub UI.
- Change 
```bash
jobs:
  build-and-unittest:
    runs-on: ubuntu-latest
```

to:

```bash
jobs:
  build-and-unittest:
    runs-on: self-hosted
``` 
If you have done it locally, commit and push the change to GitHub with this command:
```bash
git add .
git commit -m "Changed to self-hosted"
git push
``` 
- Go to github and manually trigger another pipeline run. (The pipeline will not trigger automatically unless you change something in the cloud-run-pipeline-demo directory and the workflow definition is in another directory)
- Open the log for the local runnar and when the pipeline is triggered, you will se a logentry when GitHub sends a job to it for execution.
This job contain the complete context to be able to execute the build.

That´s it for lab 3!

## Lab 4 - API versions
There is a new feature developed in a brach called api-v2-new. This feature is split between the demo application and the hub. Our job is to release the compete feature in production, with zero downtime and no conflicts. There is a new API (v2) in the Hub and some new gui in the demo application.

The way to make this happen is to deploy a new version of the Hub that service booth v1 and v2 of the api. This way, booth old and new versions of the client will work.
The course instructor will deploy a new version of the Hub. 
Make sure the central hub is running on the correct version. 
https://ci-cd-course-hub-aeyvkm6j4q-lz.a.run.app/swagger-ui.html <- you should be able to se v2 of the API here.

Your job is to:
- Create a pull request in github to merge the api-v2-new branch to main. Go to "Pull requests" in the menu. Select "Create pull request" and select api-v2-new in the right side (going in to main). 
- Merge the pull request into main. 
- This will trigger the pipeline to rebuild and deploy a new version of the demo application
- Go to the production application and verify that the new version is running there. Submit a "Happiness score"



