# spring-boot-keycloak

# Setup
## Start container `docker-compose up`
## Start application `./gradlew bootRun`
## Query application with commands

# Use application

## As admin
### Get auth key
`curl -ss --data "grant_type=password&client_id=curl&username=joe_admin&password=admin" http://localhost:8080/auth/realms/spring-security-example/protocol/openid-connect/token`

### Curl secured endpoint
`curl -H "Authorization: bearer [your-token]" http://localhost:9002/admin/hello`

## As user
### Get auth key
`curl -ss --data "grant_type=password&client_id=curl&username=jim_user&password=admin" http://localhost:8080/auth/realms/spring-security-example/protocol/openid-connect/token`

### Curl secured endpoint
`curl -H "Authorization: bearer [your-token]" http://localhost:9002/user/hello`

# Keycloak config from scratch
# Execute commands inside container
`docker exec -it springbootkeycloak_keycloak_1 /bin/bash`

# Change default user creation settings
## Edit `docker-files/keycloak/setup/start.sh`
 
# More info
[https://sandor-nemeth.github.io/java/spring/2017/06/15/spring-boot-with-keycloak.html](https://sandor-nemeth.github.io/java/spring/2017/06/15/spring-boot-with-keycloak.html)