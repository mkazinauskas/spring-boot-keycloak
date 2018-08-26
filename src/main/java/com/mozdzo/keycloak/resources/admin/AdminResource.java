package com.mozdzo.keycloak.resources.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AdminResource {
    @GetMapping("/admin")
    String helloAdmin() {
        return "Hello Admin";
    }
}
