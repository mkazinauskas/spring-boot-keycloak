package com.mozdzo.keycloak

import org.keycloak.test.TestsHelper
import org.springframework.stereotype.Component

@Component
class TokenProvider {

    String token(TestUsers.TestUser user) {
        return TestsHelper.getToken(user.username, user.password, TestsHelper.testRealm)
    }
}
