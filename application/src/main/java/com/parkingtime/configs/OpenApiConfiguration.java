package com.parkingtime.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("ParkingTime API")
                .version("1.0");
        return new OpenAPI().info(info);
    }
}
