package com.jhone.album.controller;

import com.jhone.album.dto.LoginDTO;
import com.jhone.album.service.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/login")
@Tag(name = "Login", description = "Endpoints para manutenção de login")
public class LoginController {

    private final JwtService jwtService;

    public LoginController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping
    public String login(@RequestBody LoginDTO request) {
        return jwtService.generateToken(request.getUsername());
    }

    @PostMapping("/refresh")
    public String refresh(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (jwtService.isTokenExpired(token)) {
            throw new RuntimeException("Token expirado, refaça o login");
        }

        return jwtService.generateToken(username);
    }
}
