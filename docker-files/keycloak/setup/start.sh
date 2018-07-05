#!/bin/sh

cp -r /opt/jboss/keycloak /setup/keycloak

cd /setup/keycloak/bin

./add-user-keycloak.sh -u admin -p Pa55w0rd

nohup ./standalone.sh > /dev/null 2>&1 &

until curl -q localhost:8080; do sleep 1; done

echo "Setting up users"

./kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password Pa55w0rd

./kcadm.sh create realms -s realm=spring-security-example -s enabled=true

CURL_ID=$(./kcadm.sh create clients -r spring-security-example -s clientId=curl -s enabled=true -s publicClient=true -s baseUrl=http://localhost:8080 -s adminUrl=http://localhost:8080 -s directAccessGrantsEnabled=true -i)

DEMO_APP_ID=$(./kcadm.sh create clients -r spring-security-example -s clientId=spring-security-demo-app -s enabled=true -s baseUrl=http://localhost:8080 -s bearerOnly=true -i)

./kcadm.sh create clients/$DEMO_APP_ID/roles -r spring-security-example -s name=admin -s 'description=Admin role'

./kcadm.sh create clients/$DEMO_APP_ID/roles -r spring-security-example -s name=user -s 'description=User role'

./kcadm.sh  get clients/$DEMO_APP_ID/installation/providers/keycloak-oidc-keycloak-json -r spring-security-example

JOE_ADMIN_ID=$(./kcadm.sh create users -r spring-security-example -s username=joe_admin -s enabled=true -i)

./kcadm.sh update users/$JOE_ADMIN_ID/reset-password -r spring-security-example -s type=password -s value=admin -s temporary=false -n

./kcadm.sh add-roles -r spring-security-example --uusername=joe_admin --cclientid spring-security-demo-app --rolename admin

JIM_USER_ID=$(./kcadm.sh create users -r spring-security-example -s username=jim_user -s enabled=true -i)

./kcadm.sh update users/$JIM_USER_ID/reset-password -r spring-security-example -s type=password -s value=admin -s temporary=false -n

./kcadm.sh add-roles -r spring-security-example --uusername=jim_user --cclientid spring-security-demo-app --rolename user

./jboss-cli.sh --connect command=:shutdown

nohup ./standalone.sh \
    -Dkeycloak.migration.action=export \
    -Dkeycloak.migration.provider=singleFile \
    -Dkeycloak.migration.file=import.json \
> /dev/null 2>&1 &

until curl -q localhost:8080; do sleep 1; done

./jboss-cli.sh --connect command=:shutdown

cp import.json /setup

cat /setup/import.json

rm -rf /setup/keycloak
