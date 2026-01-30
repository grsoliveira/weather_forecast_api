package domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import domain.model.DailyForecast;
import domain.model.Location;
import domain.model.WeatherResponse;
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
        .queryParam("daily", "temperature_2m_max,temperature_2m_min")
        .queryParam("forecast_days", "7")
        .queryParam("current_weather", true);

    String uri = uriBuilder.toUriString();
    JsonNode node = restTemplate.getForObject(uri, JsonNode.class);

    if (node == null || node.get("current_weather") == null) {
      return Optional.empty();
    }

    JsonNode currentWeatherNode = node.get("current_weather");
    double temperature = currentWeatherNode.get("temperature").asDouble();

    JsonNode dailyNode = node.get("daily");

    JsonNode timeNode = dailyNode.get("time");
    List<String> dates = new ArrayList<>();
    if (timeNode.isArray()) {
      for (JsonNode n : timeNode) {
        dates.add(n.asText(null));
      }
    }

    JsonNode minsNode = dailyNode.path("temperature_2m_min");
    List<Double> mins = new ArrayList<>();
    if (minsNode.isArray()) {
      for (JsonNode n : minsNode) {
        mins.add(n.isNumber() ? n.asDouble() : parseDoubleOrNull(n.asText(null)));
      }
    }

    JsonNode maxsNode = dailyNode.path("temperature_2m_max");
    List<Double> maxs = new ArrayList<>();
    if (maxsNode.isArray()) {
      for (JsonNode n : maxsNode) {
        maxs.add(n.isNumber() ? n.asDouble() : parseDoubleOrNull(n.asText(null)));
      }
    }

    List<DailyForecast> forecast = new ArrayList<>();

    for (int i = 0; i < dates.size(); i++) {
      DailyForecast day = new DailyForecast();
      day.setDate(dates.get(i));
      day.setMin(mins.get(i));
      day.setMax(maxs.get(i));
      forecast.add(day);
    }

    return Optional.of(WeatherResponse.builder()
        .zip(location.getZip())
        .currentTemperature(temperature)
        .forecast(forecast)
        .maxTemperature(maxs.get(0))
        .minTemperature(mins.get(0))
        .build());
  }

  private Double parseDoubleOrNull(String s) {
    try {
      return s == null ? 0.0 : Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }
}
