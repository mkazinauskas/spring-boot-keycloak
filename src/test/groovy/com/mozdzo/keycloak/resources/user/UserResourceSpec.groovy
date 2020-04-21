package com.mozdzo.keycloak.resources.user

import com.mozdzo.keycloak.HttpEntityBuilder
import com.mozdzo.keycloak.resources.IntegrationSpec
import org.springframework.http.ResponseEntity

import static com.mozdzo.keycloak.TestUsers.ADMIN
import static com.mozdzo.keycloak.TestUsers.USER
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED

class UserResourceSpec extends IntegrationSpec {

    def 'should access user resource with user token'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    '/user',
                    GET,
                    HttpEntityBuilder.builder()
                            .bearer(tokenProvider.token(USER))
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            response.body == 'Hello User'
    }

    def 'should forbid access to user resource with admin token'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    '/user',
                    GET,
                    HttpEntityBuilder.builder()
                            .bearer(tokenProvider.token(ADMIN))
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('Forbidden')
    }

    def 'should forbid access to user resource with no token'() {
        when:
            ResponseEntity<String> response = restTemplate.getForEntity('/user', String)
        then:
            response.statusCode == UNAUTHORIZED
    }
}
