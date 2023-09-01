package com.parkingtime.repositories;

import com.parkingtime.models.User;
import com.parkingtime.models.UserResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserResetPasswordTokenRepository extends JpaRepository<UserResetPasswordToken, Integer> {
    Integer countByUser(User user);

    @Query("SELECT t FROM UserResetPasswordToken t WHERE t.user = ?1 AND t.usedToken = false ORDER BY t.createdAt DESC")
    Optional<UserResetPasswordToken> findUserResetPasswordTokenByUser(User user);

    Optional<UserResetPasswordToken> findByToken(String token);
}
