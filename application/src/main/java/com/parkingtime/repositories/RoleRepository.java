package com.parkingtime.repositories;

import com.parkingtime.models.Role;
import com.parkingtime.common.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);

    Boolean existsByName(RoleEnum name);
}
