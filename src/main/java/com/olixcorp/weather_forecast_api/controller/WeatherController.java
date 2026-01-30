package com.olixcorp.weather_forecast_api.controller;

import domain.exception.DataNotFoundException;
import domain.model.WeatherResponse;
import domain.service.WeatherService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@AllArgsConstructor
@Validated
public class WeatherController {

  private WeatherService weatherService;

  @GetMapping
  public ResponseEntity<WeatherResponse> getWeather(@RequestParam
                                                    @NotBlank(message = "zip must not be blank")
                                                    @Pattern(regexp = "^[0-9\\- ]{1,20}$", message = "zip contains invalid characters")
                                                    String zip,
                                                    @RequestParam(defaultValue = "false")
                                                    boolean forecast) {
    WeatherResponse response = null;
    try {
      response = this.weatherService.getWeatherByZip(zip, forecast);
      if (response == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(response);
    } catch (DataNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
