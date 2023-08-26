package com.parkingtime.authentication.repositories;

import com.parkingtime.authentication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    Boolean existsByEmail(String email);
    Boolean existsByFirstnameAndLastname(String firstname, String lastname);
}
