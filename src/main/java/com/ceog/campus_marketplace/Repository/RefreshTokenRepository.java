package com.ceog.campus_marketplace.Repository;

import com.ceog.campus_marketplace.Model.RefreshToken;
import com.ceog.campus_marketplace.Model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteByUser(User user);
}