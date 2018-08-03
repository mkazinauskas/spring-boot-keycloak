package com.mozdzo.keycloak.resources;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloResource {

    @GetMapping("/admin/hello")
    String helloAdmin() {
        return "Hello Admin";
    }

    @GetMapping("/user/hello")
    String helloUser() {
        return "Hello User";
    }

    @GetMapping("/hello")
    String helloAnonymous() {
        return "Hello Anonymous";
    }
}
