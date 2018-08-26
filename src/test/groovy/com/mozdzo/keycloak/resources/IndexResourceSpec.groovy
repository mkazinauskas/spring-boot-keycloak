package com.mozdzo.keycloak.resources

import com.mozdzo.keycloak.HttpEntityBuilder
import org.springframework.http.ResponseEntity

import static com.mozdzo.keycloak.TestUsers.ADMIN
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

class IndexResourceSpec extends IntegrationSpec {

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
