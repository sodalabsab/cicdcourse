# cloud-run-hub

I am deployed to and running in the Soda Labs Google Cloud project "ci-cd-course". As such, you will find me at  
https://ci-cd-course-hub-aeyvkm6j4q-lz.a.run.app/

This hub is meant to be used together with the "participant" demo services during an instalment of the CI/CD course. The "dashboard" interface will show a card for each connected participant, once they have started and registered.

To interact with me directly, or to get an idea of how the participant services talk to me, check out my Swagger UI at  
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
docker build -t hub .
docker run --rm -p8080:8080 hub 
```
