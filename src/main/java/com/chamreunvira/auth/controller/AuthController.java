package com.chamreunvira.auth.controller;

import com.chamreunvira.auth.io.AuthRequest;
import com.chamreunvira.auth.io.AuthResponse;
import com.chamreunvira.auth.io.ResetPasswordRequest;
import com.chamreunvira.auth.service.AuthService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService profileService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        AuthResponse response = profileService.login(request);
        ResponseCookie cookie = ResponseCookie.from("token" , response.getJwtToken())
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(1))
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE , cookie.toString()).body(response);
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return ResponseEntity.ok(email != null);
    }

    @PostMapping("/send-reset-otp")
    public ResponseEntity<Void> sendResetOtp(@RequestParam("email") String email) {
        profileService.sendResetOtp(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        profileService.resetPassword(request.getNewPassword() , request.getOtp() , request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Void> sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email)  {
        profileService.sendOtp(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verifyOtp(@RequestBody Map<String , Object> request , @CurrentSecurityContext(expression = "authentication?.name") String email) {
        try {
            profileService.verifyOtp(email , request.get("otp").toString());
        }catch (Exception ex) {
            log.info(ex.getLocalizedMessage());
        }
        return ResponseEntity.ok().build();
    }

}
