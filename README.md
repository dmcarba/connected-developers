## Instructions

Clone the project and build it with mvn install.

Start the mongodb instance running the docker compose file under src/main/resources:
"docker-compose up"

Start the spring boot artifact in the folder: target.
You have to provide your own twitter app consumer crendentials (api key and secret): <https://developer.twitter.com/en/apps>

java -jar connected-developers-1.0.0-SNAPSHOT-spring-boot.jar --twitter.consumerKey=key --twitter.consumerSecret=secret