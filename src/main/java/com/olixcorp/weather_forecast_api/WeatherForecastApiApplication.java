package com.olixcorp.weather_forecast_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(scanBasePackages = {"com.olixcorp.weather_forecast_api", "domain.service"})
public class WeatherForecastApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeatherForecastApiApplication.class, args);
  }

}
