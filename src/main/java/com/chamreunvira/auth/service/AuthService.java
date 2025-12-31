package com.chamreunvira.auth.service;

import com.chamreunvira.auth.io.AuthResponse;
import com.chamreunvira.auth.io.AuthRequest;
import com.chamreunvira.auth.io.ProfileRequest;
import com.chamreunvira.auth.io.ProfileResponse;

public interface AuthService {

    ProfileResponse register(ProfileRequest request);
    AuthResponse login(AuthRequest request);
    ProfileResponse getProfile(String email);
    void sendResetOtp(String email);
    void resetPassword(String newPassword , String otp , String email);
    String getLoggedInUserId(String email);
    void sendOtp(String email);
    void verifyOtp(String email , String otp);
}
