package com.parkingtime.authentication.repositories;

import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserEmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEmailVerificationRepository extends JpaRepository<UserEmailVerification, Integer> {
    Optional<UserEmailVerification> findUserEmailVerificationByUser(User user);
    Optional<UserEmailVerification> findUserEmailVerificationByCode(int code);
}
