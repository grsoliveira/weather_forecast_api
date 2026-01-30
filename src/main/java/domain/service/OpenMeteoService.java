package domain.service;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import domain.model.Location;
import domain.model.WeatherResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OpenMeteoService {

  private final RestTemplate restTemplate = new RestTemplate();
  private final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

  public Optional<WeatherResponse> getWeatherByLocation(Location location) {

    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
        .queryParam("latitude", location.getLatitude())
        .queryParam("longitude", location.getLongitude())
        .queryParam("current_weather", true);

    String uri = uriBuilder.toUriString();
    JsonNode node = restTemplate.getForObject(uri, JsonNode.class);

    if (node == null || node.get("current_weather") == null) {
      return Optional.empty();
    }
    JsonNode currentWeatherNode = node.get("current_weather");
    double temperature = currentWeatherNode.get("temperature").asDouble();
    return Optional.of(WeatherResponse.builder()
            .zip(location.getZip())
            .currentTemperature(temperature)
        .build());
  }
}
