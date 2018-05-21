# spring-boot-keycloak

#Get auth key
`curl -ss --data "grant_type=password&client_id=curl&username=joe_admin&password=admin" http://localhost:8080/auth/realms/spring-security-example/protocol/openid-connect/token`

#use application
`curl -H "Authorization: bearer [your-token]" http://localhost:9002/admin/hello`

https://sandor-nemeth.github.io/java/spring/2017/06/15/spring-boot-with-keycloak.html