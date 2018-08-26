package com.mozdzo.keycloak.resources

import com.mozdzo.keycloak.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT

@SpringBootTest(webEnvironment = DEFINED_PORT)
abstract class IntegrationSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @Autowired
    TokenProvider tokenProvider
}
