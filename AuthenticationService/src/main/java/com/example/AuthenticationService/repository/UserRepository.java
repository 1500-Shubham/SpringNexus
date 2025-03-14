package com.example.AuthenticationService.repository;

import com.example.AuthenticationService.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (email,password,phone,username) VALUES (:email, :password, :phone, :username)", nativeQuery = true)
    void saveConnectionManually(@Param("email") String email,
                                @Param("password") String password,
                                @Param("phone") String phone,
                                @Param("username") String username);
}