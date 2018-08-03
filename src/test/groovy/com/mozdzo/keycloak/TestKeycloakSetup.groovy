package com.mozdzo.keycloak

import org.keycloak.test.TestsHelper
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(value = "test")
@Component
class TestKeycloakSetup implements InitializingBean, DisposableBean {
    private final String username
    private final String password
    private final String realm
    private final String authServerUrl
    private final String realmJsonPath

    TestKeycloakSetup(@Value('${test.keycloak.username}') String username,
                      @Value('${test.keycloak.password}') String password,
                      @Value('${keycloak.realm}') String realm,
                      @Value('${keycloak.auth-server-url}') String authServerUrl,
                      @Value('${test.keycloak.realmJsonPath}') String realmJsonPath) {
        this.username = username
        this.password = password
        this.realm = realm
        this.authServerUrl = authServerUrl
        this.realmJsonPath = realmJsonPath
    }

    @Override
    void afterPropertiesSet() throws Exception {
        TestsHelper.baseUrl = authServerUrl
        TestsHelper.testRealm = realm
        TestsHelper.keycloakBaseUrl = authServerUrl
        try {
            TestsHelper.importTestRealm(username, password, realmJsonPath)
        } catch (IOException e) {
            e.printStackTrace()
        }
        TestsHelper.createDirectGrantClient()
    }

    @Override
    void destroy() throws Exception {
        TestsHelper.deleteRealm(username, password, realm)
    }
}
