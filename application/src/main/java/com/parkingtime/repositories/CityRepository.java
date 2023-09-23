package com.parkingtime.repositories;

import com.parkingtime.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Boolean existsByName(String name);

    Optional<City> findByName(String name);
}
