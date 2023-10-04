package com.parkingtime.repositories;

import com.parkingtime.models.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<List<ParkingSpot>> findAllByGarageId(Long garageId);
}
