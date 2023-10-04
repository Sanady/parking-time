package com.parkingtime.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_garage")
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer available;

    private Integer occupied;

    private Integer total;

    private String status;

    private String type;

    @OneToOne(mappedBy = "garage",
            cascade = {
                    CascadeType.REMOVE
            })
    private GarageGeolocation garageGeolocation;

    @OneToMany(mappedBy = "garage",
            cascade = {
                    CascadeType.REMOVE
            })
    private Set<ParkingSpot> parkingSpots;
}
