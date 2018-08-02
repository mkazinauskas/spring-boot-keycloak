package com.mozdzo.keycloak;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloEndpoint {
    @GetMapping("/admin/hello")
    public String sayHelloToAdmin() {
        return "Hello Admin";
    }

    @GetMapping("/user/hello")
    public String sayHelloToUser() {
        return "Hello User";
    }
}
