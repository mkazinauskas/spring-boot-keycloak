package com.mozdzo.keycloak.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class IndexResource {
    @GetMapping("/")
    String helloAnonymous() {
        return "Hello Anonymous";
    }
}
