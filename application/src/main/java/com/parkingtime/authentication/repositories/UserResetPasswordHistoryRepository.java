package com.parkingtime.authentication.repositories;

import com.parkingtime.authentication.models.UserResetPasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResetPasswordHistoryRepository extends JpaRepository<UserResetPasswordHistory, Integer> {
}
