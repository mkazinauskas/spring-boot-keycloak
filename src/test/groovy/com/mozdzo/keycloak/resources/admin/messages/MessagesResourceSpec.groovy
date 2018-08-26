package com.mozdzo.keycloak.resources.admin.messages

import com.mozdzo.keycloak.HttpEntityBuilder
import com.mozdzo.keycloak.resources.IntegrationSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity

import static com.mozdzo.keycloak.TestUsers.ADMIN
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK

@SpringBootTest(webEnvironment = DEFINED_PORT)
class MessagesResourceSpec extends IntegrationSpec {

    def 'should access admin resource with admin token'() {
        given:
            CreateMessageRequest request = new CreateMessageRequest(message: 'My message')
        when:
            ResponseEntity<String> createMessageResponse = restTemplate.exchange(
                    '/admin/messages',
                    POST,
                    HttpEntityBuilder.builder()
                            .bearer(tokenProvider.token(ADMIN))
                            .body(request)
                            .build(),
                    String
            )
        then:
            createMessageResponse.statusCode == CREATED
            !createMessageResponse.body
        and:
            long messageId = createMessageResponse.headers.getLocation().path.split('/').last() as long
        when:
            ResponseEntity<Message> retrieveMessageResponse = restTemplate.exchange(
                    "/admin/messages/${messageId}",
                    GET,
                    HttpEntityBuilder.builder()
                            .bearer(tokenProvider.token(ADMIN))
                            .build(),
                    Message
            )
        then:
            retrieveMessageResponse.statusCode == OK
        and:
            retrieveMessageResponse.body.id == messageId
            retrieveMessageResponse.body.message == request.message
    }
}
