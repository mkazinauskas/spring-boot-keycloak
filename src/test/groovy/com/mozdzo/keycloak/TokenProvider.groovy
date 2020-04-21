package com.mozdzo.keycloak

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.testcontainers.shaded.com.google.common.base.Preconditions

@Component
class TokenProvider {

    private final String authServerUrl

    private final String realm

    private final TestRestTemplate restTemplate

    TokenProvider(@Value('${keycloak.auth-server-url}') String authServerUrl,
                  @Value('${keycloak.realm}') String realm,
                  TestRestTemplate restTemplate) {
        this.authServerUrl = authServerUrl
        this.realm = realm
        this.restTemplate = restTemplate
    }

    String token(TestUsers.TestUser user) {
        String url = "${authServerUrl}/realms/${realm}/protocol/openid-connect/token"

        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>()
        map.add('grant_type', 'password')
        map.add('client_id', 'test-dga')
        map.add('username', user.username)
        map.add('password', user.password)

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers)

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map)

        Preconditions.checkArgument(response.statusCode == HttpStatus.OK)

        return response.body.access_token
    }
}
