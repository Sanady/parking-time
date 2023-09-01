package com.parkingtime.repositories;

import com.parkingtime.models.UserResetPasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResetPasswordHistoryRepository extends JpaRepository<UserResetPasswordHistory, Integer> {
}
