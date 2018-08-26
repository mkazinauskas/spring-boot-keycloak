package com.mozdzo.keycloak.resources.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserResource {

    @GetMapping("/user")
    String helloUser() {
        return "Hello User";
    }
}