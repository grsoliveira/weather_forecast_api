package com.olixcorp.weather_forecast_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
  private String zip;
  private double currentTemperature;
}
