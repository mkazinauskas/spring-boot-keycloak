package com.mozdzo.keycloak.setup

import groovy.util.logging.Slf4j
import org.keycloak.test.TestsHelper
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.containers.wait.strategy.WaitStrategy

import static java.time.Duration.ofSeconds

@ActiveProfiles(value = 'test')
@Component
@Slf4j
class TestKeycloakSetup implements InitializingBean, DisposableBean {
    private final TestKeycloakConfiguration keycloakConfig
    private final String realm
    private final String authServerUrl
    private final KeycloakContainer container

    TestKeycloakSetup(TestKeycloakConfiguration keycloakConfig,
                      @Value('${keycloak.realm}') String realm,
                      @Value('${keycloak.auth-server-url}') String authServerUrl) {
        this.keycloakConfig = keycloakConfig
        this.realm = realm
        this.authServerUrl = authServerUrl
        this.container = new KeycloakContainer(
                keycloakConfig.username, keycloakConfig.password, keycloakConfig.port
        )
    }

    @Override
    void afterPropertiesSet() throws Exception {
        container.start()

        TestsHelper.baseUrl = authServerUrl
        TestsHelper.testRealm = realm
        TestsHelper.keycloakBaseUrl = authServerUrl
        try {
            TestsHelper.importTestRealm(keycloakConfig.username, keycloakConfig.password, keycloakConfig.realmJsonPath)
        } catch (IOException e) {
            log.error('Failed to import data to ream', e)
            throw new IllegalStateException('Failed to setup keycloak realm!')
        }
        TestsHelper.createDirectGrantClient()
    }

    @Override
    void destroy() throws Exception {
        TestsHelper.deleteRealm(keycloakConfig.username, keycloakConfig.password, realm)
        container.stop()
    }

    static class KeycloakContainer {
        private final static int KEYCLOAK_INTERNAL_PORT = 8080

        private final static WaitStrategy WAITING_STRATEGY = new HttpWaitStrategy()
                .forPath("/")
                .forPort(KEYCLOAK_INTERNAL_PORT)

        private final GenericContainer container

        KeycloakContainer(String username, String password, int port) {
            this.container = new FixedHostPortGenericContainer('jboss/keycloak:7.0.0')
                    .withFixedExposedPort(port, KEYCLOAK_INTERNAL_PORT)
                    .withEnv(['KEYCLOAK_USER'    : username,
                              'KEYCLOAK_PASSWORD': password,
                              'DB_VENDOR'        : 'h2'])
                    .withMinimumRunningDuration(ofSeconds(10))
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
