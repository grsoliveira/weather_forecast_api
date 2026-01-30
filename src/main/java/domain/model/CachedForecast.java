package domain.model;

import java.time.Duration;
import java.time.Instant;

public class CachedForecast {

  private WeatherResponse weatherResponse;
  private Instant cachedAt;

  public CachedForecast(
      WeatherResponse weatherResponse,
      Instant cachedAt) {
    this.weatherResponse = weatherResponse;
    this.cachedAt = cachedAt;
  }

  public boolean isExpired(Duration cacheDuration) {
    return cachedAt.plus(cacheDuration).isBefore(Instant.now());
  }

  public WeatherResponse getWeatherResponse() {
    return weatherResponse;
  }

}
