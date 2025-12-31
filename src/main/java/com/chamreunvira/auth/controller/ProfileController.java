package com.chamreunvira.auth.controller;

import com.chamreunvira.auth.io.ProfileRequest;
import com.chamreunvira.auth.io.ProfileResponse;
import com.chamreunvira.auth.service.AuthService;
import com.chamreunvira.auth.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ProfileController {

    private final AuthService profileService;
    private final MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<ProfileResponse> register(@RequestBody ProfileRequest request) {
        ProfileResponse response = profileService.register(request);
        mailService.sendWelcomeEmail(response.getEmail(), response.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> profile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return ResponseEntity.ok().body(profileService.getProfile(email));
    }

}