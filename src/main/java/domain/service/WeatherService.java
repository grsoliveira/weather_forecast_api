package domain.service;

import domain.model.Location;
import domain.model.WeatherResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeatherService {

  private GeocodingService geocodingService;

  public WeatherResponse getWeatherByZip(String zip) {

    Location locationByZip = this.geocodingService.getLocationByZip(zip);
    System.out.println("Location for zip " + zip + ": " + locationByZip);

    //TODO remove mock implementation
    return new WeatherResponse(zip, 25.0);
  }
}
