package com.parkingtime;

import com.parkingtime.models.Role;
import com.parkingtime.repositories.RoleRepository;
import com.parkingtime.common.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Slf4j
@EnableScheduling
@EnableWebMvc
@SpringBootApplication
public class ParkingTimeApplication implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(ParkingTimeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if(roleRepository.existsByName(RoleEnum.ROLE_USER) == Boolean.FALSE &&
                roleRepository.existsByName(RoleEnum.ROLE_MODERATOR) == Boolean.FALSE &&
                roleRepository.existsByName(RoleEnum.ROLE_ADMIN) == Boolean.FALSE) {
            Role userRole = new Role(1L, RoleEnum.ROLE_USER);
            Role modRole = new Role(2L, RoleEnum.ROLE_MODERATOR);
            Role adminRole = new Role(3L, RoleEnum.ROLE_ADMIN);
            roleRepository.saveAll(List.of(userRole, modRole, adminRole));
            log.info("Roles have been successfully added");
        }
    }
}
