package domain.service;

import java.util.Optional;

import domain.model.Location;
import domain.model.WeatherResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeatherService {

  private GeocodingService geocodingService;

  public WeatherResponse getWeatherByZip(String zip) {

    Optional<Location> locationByZip = this.geocodingService.getLocationByZip(zip);
    System.out.println("Location for zip " + zip + ": " + locationByZip.get());

    //TODO remove mock implementation
    return new WeatherResponse(zip, 25.0);
  }
}
