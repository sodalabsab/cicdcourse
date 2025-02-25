# cloud-run-hub

I am deployed to and running in the Soda Labs Google Cloud project "ci-cd-course". As such, you will
find me at  
https://ci-cd-course-hub-aeyvkm6j4q-lz.a.run.app/

This hub is meant to be used together with the "participant" demo services during an instalment of
the CI/CD course. The "dashboard" interface will show a card for each connected participant, once
they have started and registered.

The participant service that connects to this hub lives
here: https://github.com/sodalabsab/cloud-run-pipeline-demo - follow the README there to fork that
repository when participating in the CI/CD course.

To interact with me directly, or to get an idea of how the participant services talk to me, check
out my Swagger UI at  
https://ci-cd-course-hub-aeyvkm6j4q-lz.a.run.app/swagger-ui/index.html  
or, if running locally, http://localhost:8080/swagger-ui/index.html

## Running Locally, you say?

Of course. Just

```
./gradlew
java -jar build/libs/hub-<version>.jar
``` 

or

```
./gradlew
docker build -t hub .
docker run --rm -p8080:8080 hub 
```

if you prefer running a Docker container.

or

```
docker-compose up 
```

in the root directory of this repo to start booth the hub and a demo client locally.

## Development Practices

🔡 Follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) for
formatting. Find instructions here for IntelliJ plugins
etc.: https://github.com/google/google-java-format.

✅ Try to adhere to [conventional commits](https://www.conventionalcommits.org/en/v1.0.0/) when
making changes.

💟 Try to [make your code reviewer fall in love with you](https://mtlynch.io/code-review-love/) when
creating pull requests.
