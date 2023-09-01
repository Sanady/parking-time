package com.parkingtime.repositories;

import com.parkingtime.models.User;
import com.parkingtime.models.UserEmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEmailVerificationRepository extends JpaRepository<UserEmailVerification, Integer> {
    Optional<UserEmailVerification> findUserEmailVerificationByUser(User user);
    Optional<UserEmailVerification> findUserEmailVerificationByCode(int code);
}
