package com.mozdzo.keycloak.setup

import groovy.util.logging.Slf4j
import org.keycloak.test.FluentTestsHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@Slf4j
class KeycloakTestHelper {

    private final String realm

    private final String realmJsonPath

    private final FluentTestsHelper fluentTestsHelper

    KeycloakTestHelper(TestKeycloakConfiguration keycloakConfig,
                       @Value('${keycloak.realm}') String realm,
                       @Value('${keycloak.auth-server-url}') String authServerUrl) {
        this.realm = realm
        this.realmJsonPath = keycloakConfig.realmJsonPath

        String keycloakBaseUrl = authServerUrl
        String adminUserName = keycloakConfig.username
        String adminPassword = keycloakConfig.password
        String adminRealm = FluentTestsHelper.DEFAULT_ADMIN_REALM
        String adminClient = FluentTestsHelper.DEFAULT_ADMIN_CLIENT
        String testRealm = FluentTestsHelper.DEFAULT_TEST_REALM

        this.fluentTestsHelper = new FluentTestsHelper(
                keycloakBaseUrl,
                adminUserName,
                adminPassword,
                adminRealm,
                adminClient,
                testRealm
        )
    }

    void init() {
        fluentTestsHelper.init()

        try {
            fluentTestsHelper.importTestRealm(this.realmJsonPath)
        } catch (IOException e) {
            log.error('Failed to import data to ream', e)
            throw new IllegalStateException('Failed to setup keycloak realm!')
        }
        fluentTestsHelper.createDirectGrantClient('test-dga')
    }

    void destroy() {
        fluentTestsHelper.deleteRealm(realm)
    }
}