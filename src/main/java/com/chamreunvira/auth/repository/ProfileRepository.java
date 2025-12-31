package com.chamreunvira.auth.repository;

import com.chamreunvira.auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<UserEntity , Long> {

    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

}
