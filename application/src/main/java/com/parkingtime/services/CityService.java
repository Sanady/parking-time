package com.parkingtime.services;

import com.parkingtime.common.requests.CreateCityRequest;
import com.parkingtime.common.responses.CreateCityResponse;
import com.parkingtime.models.City;
import com.parkingtime.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.parkingtime.common.constants.ApplicationConstants.CITY_REGEX;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public CreateCityResponse createCity(CreateCityRequest request) {
        if(!Pattern.compile(CITY_REGEX).matcher(request.getName()).matches()) {
            throw new IllegalArgumentException("City name is invalid");
        }

        if(Boolean.TRUE.equals(cityRepository.existsByName(request.getName()))) {
            throw new IllegalArgumentException("City already exists");
        }

        cityRepository.save(new City(request.getName()));
        return CreateCityResponse.builder()
                .name(request.getName())
                .build();
    }

    public City getCityByName(String name) {
        return cityRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("City not found"));
    }

    public void deleteCityByName(String name) {
        City city = getCityByName(name);
        cityRepository.delete(city);
    }


}
