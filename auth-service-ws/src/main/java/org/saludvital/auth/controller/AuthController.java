package org.saludvital.auth.controller;

import java.util.List;
import java.util.Map;
import org.saludvital.auth.dto.LoginRequest;
import org.saludvital.auth.security.JwtIssuer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtIssuer issuer;
    public AuthController(JwtIssuer issuer) { this.issuer = issuer; }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        List<String> roles = "admin".equalsIgnoreCase(request.username()) ? List.of("ROLE_ADMIN") : List.of("ROLE_USER");
        return Map.of("token", issuer.issue(request.username(), roles));
    }
}
