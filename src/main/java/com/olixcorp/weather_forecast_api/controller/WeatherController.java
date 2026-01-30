package com.olixcorp.weather_forecast_api.controller;

import domain.model.WeatherResponse;
import domain.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@AllArgsConstructor
public class WeatherController {

  private WeatherService weatherService;

  @GetMapping()
  public WeatherResponse getWeather(@RequestParam String zip,
                                    @RequestParam(defaultValue = "false") boolean forecast) {
    return this.weatherService.getWeatherByZip(zip, forecast);
  }

}
