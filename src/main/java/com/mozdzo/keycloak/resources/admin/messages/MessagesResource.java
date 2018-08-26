package com.mozdzo.keycloak.resources.admin.messages;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static java.net.URI.create;
import static java.util.concurrent.ThreadLocalRandom.current;

@RestController
class MessagesResource {

    private static final Map<Long, Message> MESSAGES = new ConcurrentHashMap<>();

    @PostMapping("/admin/messages")
    ResponseEntity createMessage(@Valid @RequestBody CreateMessageRequest request) {
        long newMessageId = current().nextLong(10000);
        MESSAGES.put(newMessageId, new Message(newMessageId, request.getMessage()));
        return ResponseEntity
                .created(create(format("/admin/messages/%s", newMessageId)))
                .build();
    }

    @GetMapping("/admin/messages/{id}")
    ResponseEntity getMessage(@PathVariable("id") long id) {
        return ResponseEntity.ok(MESSAGES.get(id));
    }
}
