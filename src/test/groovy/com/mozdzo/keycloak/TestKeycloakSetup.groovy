package com.mozdzo.keycloak

import org.keycloak.test.TestsHelper
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TestKeycloakSetup implements InitializingBean, DisposableBean {
    private final String username
    private final String password
    private final String realm
    private final String authServerUrl

    TestKeycloakSetup(@Value('${app.username}') String username,
                      @Value('${app.password}') String password,
                      @Value('${keycloak.realm}') String realm,
                      @Value('${keycloak.auth-server-url}') String authServerUrl) {
        this.username = username
        this.password = password
        this.realm = realm
        this.authServerUrl = authServerUrl
    }

    @Override
    void afterPropertiesSet() throws Exception {
        TestsHelper.baseUrl = authServerUrl
        TestsHelper.testRealm = realm
        try {
            TestsHelper.importTestRealm(username, password, '/quickstart-realm.json')
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
