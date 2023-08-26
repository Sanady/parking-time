package com.parkingtime.authentication.repositories;

import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.Vehicle;
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
