package com.chamreunvira.auth.service;

import com.chamreunvira.auth.model.UserEntity;
import com.chamreunvira.auth.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = profileRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found with email " + email));

        return new User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
