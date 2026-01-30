package domain.service;

import domain.model.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

  public WeatherResponse getWeatherByZip(String zip) {
    //TODO remove mock implementation
    return new WeatherResponse(zip, 25.0);
  }
}
