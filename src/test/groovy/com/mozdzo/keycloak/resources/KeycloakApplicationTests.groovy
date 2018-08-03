package com.mozdzo.keycloak.resources

import com.mozdzo.keycloak.HttpEntityBuilder
import com.mozdzo.keycloak.TestUsers
import com.mozdzo.keycloak.TokenProvider
import org.keycloak.test.TestsHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.core.token.TokenService
import spock.lang.Specification

import static com.mozdzo.keycloak.TestUsers.ADMIN
import static com.mozdzo.keycloak.TestUsers.USER
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.*

@SpringBootTest(webEnvironment = DEFINED_PORT)
class KeycloakApplicationTests extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @Autowired
    TokenProvider tokenProvider

    def 'should access admin resource with admin token'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    '/admin/hello',
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
                    '/admin/hello',
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
            ResponseEntity<String> response = restTemplate.getForEntity('/admin/hello', String)
        then:
            response.statusCode == UNAUTHORIZED
    }
}
