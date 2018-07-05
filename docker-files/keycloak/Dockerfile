FROM jboss/keycloak:4.1.0.Final

ADD setup /setup

USER root

RUN chmod 777 /setup/import.json

RUN ps aux | grep jboss

RUN ps aux | grep standalone

RUN cp /setup/import.json /opt/jboss/keycloak/import.json

#USER root
#
#RUN /setup/start.sh
#USER jboss

#"-Dkeycloak.migration.file=\"/setup/import.json\"", \