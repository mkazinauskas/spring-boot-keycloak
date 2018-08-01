# spring-boot-keycloak

# Setup
1. Start container `docker/keycloak` with command `docker-compose up`
2. Start application `./gradlew bootRun`
3. Query application with commands

# Use application

## As admin
### Get auth key
`curl -ss --data "grant_type=password&client_id=curl&username=joe_admin&password=admin" http://localhost:8081/auth/realms/spring-security-example/protocol/openid-connect/token`

### Curl secured endpoint
`curl -H "Authorization: bearer [your-token]" http://localhost:8080/admin/hello`

## As user
### Get auth key
`curl -ss --data "grant_type=password&client_id=curl&username=jim_user&password=admin" http://localhost:8081/auth/realms/spring-security-example/protocol/openid-connect/token`

### Curl secured endpoint
`curl -H "Authorization: bearer [your-token]" http://localhost:8080/user/hello`

# Keycloak config from scratch
# Execute commands inside container
`docker exec -it springbootkeycloak_keycloak_1 /bin/bash`

# Update realms and users
## Edit `docker/keycloak/setup/setup.sh`
## Run this file inside your local keycloak installation bin folder


Testing
https://github.com/keycloak/keycloak-quickstarts/blob/latest/app-authz-rest-springboot/pom.xml