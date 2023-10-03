package com.parkingtime.repositories;

import com.parkingtime.models.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    Optional<Garage> findByName(String name);
    Boolean existsByName(String name);
}
