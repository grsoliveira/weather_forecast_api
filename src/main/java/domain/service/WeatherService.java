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
  private OpenMeteoService openMeteoService;

  public WeatherResponse getWeatherByZip(String zip) {

    Optional<Location> locationByZip = this.geocodingService.getLocationByZip(zip);
    System.out.println("Location for zip " + zip + ": " + locationByZip.get());

    Optional<WeatherResponse> weatherByLocation = this.openMeteoService.getWeatherByLocation(locationByZip.get());
    System.out.println("Weather for location " + locationByZip.get() + ": " + weatherByLocation.get());

    return weatherByLocation.get();
  }
}
