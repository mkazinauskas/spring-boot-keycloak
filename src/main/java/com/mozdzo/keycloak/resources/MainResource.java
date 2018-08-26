package com.mozdzo.keycloak.resources;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MainResource {

    @GetMapping("/admin")
    String helloAdmin() {
        return "Hello Admin";
    }

    @GetMapping("/user")
    String helloUser() {
        return "Hello User";
    }

    @GetMapping("/")
    String helloAnonymous() {
        return "Hello Anonymous";
    }
}
