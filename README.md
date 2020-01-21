## Instructions

Clone the project and build it with mvn install.

Start the mongodb instance running the docker compose file under src/main/resources:
"docker-compose up"

The spring boot jar artifact is generated in the target folder
You have to provide your own twitter app consumer credentials (api key and secret): <https://developer.twitter.com/en/apps> to
run the service

java -jar connected-developers-1.0.0-SNAPSHOT-spring-boot.jar --twitter.consumerKey=key --twitter.consumerSecret=secret

The default port is 8080 it can be changed with --server.port

Endpoints:

http://localhost:8080/connected/realtime/dev1/dev2

http://localhost:8080/connected/register/dev1/dev2

