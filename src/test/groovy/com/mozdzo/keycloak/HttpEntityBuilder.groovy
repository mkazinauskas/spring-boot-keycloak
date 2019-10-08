package com.mozdzo.keycloak

import groovy.transform.CompileStatic
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders

import java.nio.charset.Charset

@CompileStatic
class HttpEntityBuilder<B> {

    private B body

    private final HttpHeaders headers = new HttpHeaders()

    static HttpEntityBuilder builder() {
        return new HttpEntityBuilder()
    }

    HttpEntityBuilder<B> body(B body) {
        this.body = body
        return this
    }

    HttpEntityBuilder<B> bearer(String token) {
        headers.add('Authorization', "Bearer ${token}".toString())
        return this
    }

    HttpEntityBuilder<B> basic(String username, String password) {
        String auth = "${username}:${password}"

        String encoded = Base64.encoder.encodeToString(auth.getBytes(Charset.forName('US-ASCII')))

        headers.add('Authorization', "Basic ${encoded}")
        return this
    }

    HttpEntity build() {
        return new HttpEntity(body, headers)
    }
}
