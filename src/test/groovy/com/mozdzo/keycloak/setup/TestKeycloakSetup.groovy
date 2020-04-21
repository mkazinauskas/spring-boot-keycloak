package com.mozdzo.keycloak.setup

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.DisposableBean
import org.springframework.stereotype.Component
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.containers.wait.strategy.WaitStrategy

import javax.annotation.PostConstruct

import static java.time.Duration.ofSeconds

@Component
@Slf4j
class TestKeycloakSetup implements DisposableBean {

    private final KeycloakContainer container
    private final KeycloakTestHelper keycloakTestHelper

    TestKeycloakSetup(TestKeycloakConfiguration keycloakConfig,
                      KeycloakTestHelper testHelper
    ) {
        this.keycloakTestHelper = testHelper
        this.container = new KeycloakContainer(
                keycloakConfig.username, keycloakConfig.password, keycloakConfig.port
        )
    }

    @PostConstruct
    void initialize() throws Exception {
        container.start()
        keycloakTestHelper.init()
    }

    @Override
    void destroy() throws Exception {
        keycloakTestHelper.destroy()
        container.stop()
    }

    static class KeycloakContainer {
        private final static int KEYCLOAK_INTERNAL_PORT = 8080

        private final static WaitStrategy WAITING_STRATEGY = new HttpWaitStrategy()
                .forPath('/')
                .forPort(KEYCLOAK_INTERNAL_PORT)

        private final GenericContainer container

        KeycloakContainer(String username, String password, int port) {
            this.container = new FixedHostPortGenericContainer('jboss/keycloak:9.0.3')
                    .withFixedExposedPort(port, KEYCLOAK_INTERNAL_PORT)
                    .withEnv(['KEYCLOAK_USER'    : username,
                              'KEYCLOAK_PASSWORD': password,
                              'DB_VENDOR'        : 'h2'])
                    .withMinimumRunningDuration(ofSeconds(10))
                    .withLogConsumer()
                    .waitingFor(WAITING_STRATEGY)
        }

        void start() {
            container.start()
        }

        void stop() {
            container.stop()
        }
    }
}
