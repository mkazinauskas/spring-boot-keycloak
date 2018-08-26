package com.mozdzo.keycloak.resources.admin

import com.mozdzo.keycloak.HttpEntityBuilder
import com.mozdzo.keycloak.resources.IntegrationSpec
import org.springframework.http.ResponseEntity

import static com.mozdzo.keycloak.TestUsers.ADMIN
import static com.mozdzo.keycloak.TestUsers.USER
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.*

class AdminResourceSpec extends IntegrationSpec {

    def 'should access admin resource with admin token'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    '/admin',
                    GET,
                    HttpEntityBuilder.builder()
                            .bearer(tokenProvider.token(ADMIN))
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            response.body == 'Hello Admin'
    }

    def 'should forbid access to admin resource with user token'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    '/admin',
                    GET,
                    HttpEntityBuilder.builder()
                            .bearer(tokenProvider.token(USER))
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('Forbidden')
    }

    def 'should forbid access to admin resource with no token'() {
        when:
            ResponseEntity<String> response = restTemplate.getForEntity('/admin', String)
        then:
            response.statusCode == UNAUTHORIZED
    }
}