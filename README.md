# spring-boot-keycloak

# Setup
## Start container `docker-compose up`
## Start application `./gradlew bootRun`
## Query application with commands from text section

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
# Execute commands
`docker exec -it springbootkeycloak_keycloak_1 /bin/bash`

`cd keycloak/bin`

`./kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password Pa55w0rd`

`./kcadm.sh create realms -s realm=spring-security-example -s enabled=true`

`./kcadm.sh create clients -r spring-security-example -s clientId=curl -s enabled=true -s publicClient=true -s baseUrl=http://localhost:8080 -s adminUrl=http://localhost:8080 -s directAccessGrantsEnabled=true`
Created new client with id '2ade7228-0a6a-445b-9bfb-85a702aad6d0'

`./kcadm.sh create clients -r spring-security-example -s clientId=spring-security-demo-app -s enabled=true -s baseUrl=http://localhost:8080 -s bearerOnly=true`
Created new client with id 'ced67303-deb6-481f-988b-44c96f98e7a4'

`./kcadm.sh create clients/ced67303-deb6-481f-988b-44c96f98e7a4/roles -r spring-security-example -s name=admin -s 'description=Admin role'`
Created new role with id 'admin'

`./kcadm.sh create clients/ced67303-deb6-481f-988b-44c96f98e7a4/roles -r spring-security-example -s name=user -s 'description=User role'`
Created new role with id 'user'


`./kcadm.sh  get clients/ced67303-deb6-481f-988b-44c96f98e7a4/installation/providers/keycloak-oidc-keycloak-json -r spring-security-example`
```json
{
  "realm" : "spring-security-example",
  "bearer-only" : true,
  "auth-server-url" : "http://localhost:8080/auth",
  "ssl-required" : "external",
  "resource" : "spring-security-demo-app",
  "use-resource-role-mappings" : true,
  "confidential-port" : 0
}
```
`./kcadm.sh create users -r spring-security-example -s username=joe_admin -s enabled=true`
Created new user with id '1cbb864c-0c1f-499a-b4fc-c0a9d14609cd'

`./kcadm.sh update users/1cbb864c-0c1f-499a-b4fc-c0a9d14609cd/reset-password -r spring-security-example -s type=password -s value=admin -s temporary=false -n`

`./kcadm.sh add-roles -r spring-security-example --uusername=joe_admin --cclientid spring-security-demo-app --rolename admin`

`./kcadm.sh create users -r spring-security-example -s username=jim_user -s enabled=true`
Created new user with id '51a709a7-c0a3-4604-80c5-838c202fbbbc'

`./kcadm.sh update users/51a709a7-c0a3-4604-80c5-838c202fbbbc/reset-password -r spring-security-example -s type=password -s value=admin -s temporary=false -n`

`./kcadm.sh add-roles -r spring-security-example --uusername=jim_user --cclientid spring-security-demo-app --rolename user`

# Import config from file
## Login to `http://localhost:8080/auth/admin/master/console` with credentials `admin` and `Pa55w0rd`
## Please use `realm-export.json` file to import into keycloak system.
 
https://sandor-nemeth.github.io/java/spring/2017/06/15/spring-boot-with-keycloak.html