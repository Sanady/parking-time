package com.parkingtime.repositories;

import com.parkingtime.models.GarageGeolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GarageGeolocationRepository extends JpaRepository<GarageGeolocation, Long> {
    Optional<GarageRepository> findByLatitudeAndLongitude(Double latitude, Double longitude);
    Boolean existsByLatitudeAndLongitude(Double latitude, Double longitude);
}
