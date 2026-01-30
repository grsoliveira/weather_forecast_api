package domain.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import domain.model.CachedForecast;
import domain.model.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
public class WeatherCacheService {

  private static final Duration CACHE_DURATION = Duration.ofMinutes(15);

  private final Map<String, CachedForecast> cache = new ConcurrentHashMap<>();

  public Optional<WeatherResponse> getCachedForecast(String zip) {

    CachedForecast cached = cache.get(zip);

    if (cached == null || cached.isExpired(CACHE_DURATION)) {
      cache.remove(zip);
      return Optional.empty();
    }

    return Optional.of(cached.getWeatherResponse());
  }

  public void save(String zip, WeatherResponse response) {
    cache.put(zip,
        new CachedForecast(response, Instant.now()));
  }
}
