package com.parkingtime.repositories;

import com.parkingtime.models.User;
import com.parkingtime.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Optional<Vehicle> findByLicencePlate(String licencePlate);
    Optional<Vehicle> findById(Long id);

    Integer countVehiclesByUser(User user);

    Boolean existsVehicleByUserAndLicencePlate(User user, String licencePlate);
}
