package com.olixcorp.weather_forecast_api.service;

import com.olixcorp.weather_forecast_api.model.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

  public WeatherResponse getWeatherByZip(String zip) {
    //TODO remove mock implementation
    return new WeatherResponse(zip, 25.0);
  }
}
