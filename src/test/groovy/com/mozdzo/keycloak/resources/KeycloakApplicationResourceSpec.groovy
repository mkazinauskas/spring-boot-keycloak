package com.mozdzo.keycloak.resources

import com.mozdzo.keycloak.HttpEntityBuilder
import com.mozdzo.keycloak.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.RequestBuilder
import spock.lang.Specification

import static com.mozdzo.keycloak.TestUsers.ADMIN
import static com.mozdzo.keycloak.TestUsers.USER
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.*

@SpringBootTest(webEnvironment = DEFINED_PORT)
class KeycloakApplicationResourceSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @Autowired
    TokenProvider tokenProvider

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

            RequestBuilder
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
            ResponseEntity<String> response = restTemplate.getForEntity('/admin/hello', String)
        then:
            response.statusCode == UNAUTHORIZED
    }

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

            RequestBuilder
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
            ResponseEntity<String> response = restTemplate.getForEntity('/user/hello', String)
        then:
            response.statusCode == UNAUTHORIZED
    }

    def 'should allow access public resource with admin token'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    '/',
                    GET,
                    HttpEntityBuilder.builder()
                            .bearer(tokenProvider.token(ADMIN))
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            response.body == 'Hello Anonymous'
    }

    def 'should allow to access public resource'() {
        when:
            ResponseEntity<String> response = restTemplate.getForEntity('/', String)
        then:
            response.statusCode == OK
            response.body == 'Hello Anonymous'
    }
}
