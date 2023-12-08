package com.tdtu.backend.repository;

import com.tdtu.backend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    List<VerificationToken> findByUserId(Long id);
}
