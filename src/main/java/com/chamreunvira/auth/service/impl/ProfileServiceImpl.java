package com.chamreunvira.auth.service.impl;

import com.chamreunvira.auth.io.AuthResponse;
import com.chamreunvira.auth.io.AuthRequest;
import com.chamreunvira.auth.io.ProfileRequest;
import com.chamreunvira.auth.io.ProfileResponse;
import com.chamreunvira.auth.model.UserEntity;
import com.chamreunvira.auth.repository.ProfileRepository;
import com.chamreunvira.auth.service.AuthService;
import com.chamreunvira.auth.service.MailService;
import com.chamreunvira.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements AuthService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MailService mailService;

    @Override
    public ProfileResponse register(ProfileRequest request) {
        if(Objects.isNull(request.getPassword())) {
            throw new IllegalArgumentException("Password can not be blank or null");
        }
        if(profileRepository.existsByEmail(request.getEmail())) {
            throw new UsernameNotFoundException("Email is ready exists with " + request.getEmail());
        }
        UserEntity user = mapperToEntity(request);
        profileRepository.save(user);
        return mapperToResponse(user);
    }

    @Override
    public AuthResponse login(AuthRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        final UserEntity user = profileRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email not found with email " + request.getEmail()));

        final String jwtToken = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .email(user.getEmail())
                .jwtToken(jwtToken)
                .build();
    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity user = profileRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with " + email));
        return mapperToResponse(user);
    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity user = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with " + email));
        String opt = String.valueOf(ThreadLocalRandom.current().nextInt(100000 , 1000000));

        long expiryTime = System.currentTimeMillis() + (60 * 10 * 1000);

        user.setResetOtp(opt);
        user.setResetOtpExpireAt(expiryTime);

        profileRepository.save(user);

        try {
            mailService.sendResetOptEmail(user.getEmail() ,opt);
        }catch(Exception ex) {
            throw new RuntimeException("Unable to send email.");
        }

    }

    @Override
    public void resetPassword(String newPassword, String otp, String email) {
        UserEntity user = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with " + email));

        if(user.getResetOtp() == null || !user.getResetOtp().equals(otp)) {
            throw new RuntimeException("Otp is valid.");
        }

        if(user.getResetOtpExpireAt() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP is expiration.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setResetOtpExpireAt(0L);

        profileRepository.save(user);
    }

    @Override
    public String getLoggedInUserId(String email) {
        UserEntity user = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with " + email));
        return user.getUserId();
    }

    @Override
    public void sendOtp(String email) {


        UserEntity user = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with " + email));

        if(user.getVerifyOtp() != null && user.isAccountVerified()) {
            return;
        }

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Long expiryTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);

        user.setVerifyOtp(otp);
        user.setVerifyOtpExpireAt(expiryTime);

        profileRepository.save(user);

        try {
            mailService.sendOtpEmail(email , otp);
        }catch (Exception ex) {
            throw new RuntimeException("Unable to send email" + ex.getMessage());
        }

    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity user = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with " + email));

        if(user.getVerifyOtp() == null || !user.getVerifyOtp().equals(otp)) {
            throw new RuntimeException("OTP is incorrect.");
        }

        if(user.getVerifyOtpExpireAt() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP is expiration");
        }

        user.setAccountVerified(true);
        user.setVerifyOtp(null);
        user.setVerifyOtpExpireAt(0L);

        profileRepository.save(user);

    }

    private ProfileResponse mapperToResponse(UserEntity userEntity) {
        return ProfileResponse.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .build();
    }

    private UserEntity mapperToEntity(ProfileRequest req) {
        return UserEntity.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .userId(UUID.randomUUID().toString())
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .isAccountVerified(false)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .build();
    }
}
