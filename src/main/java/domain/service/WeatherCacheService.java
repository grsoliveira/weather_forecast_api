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

  public Optional<WeatherResponse> getCachedForecast(String zip, boolean forecast) {
    CachedForecast cached = cache.get(zip);
    if (cached == null || cached.isExpired(CACHE_DURATION)) {
      cache.remove(zip);
      return Optional.empty();
    }

    //To avoid modifications on a cached object, I create a copy
    WeatherResponse copy = WeatherResponse.builder()
        .zip(cached.getWeatherResponse().getZip())
        .currentTemperature(cached.getWeatherResponse().getCurrentTemperature())
        .fromCache(cached.getWeatherResponse().getFromCache())
        .minTemperature((forecast) ? cached.getWeatherResponse().getMinTemperature() : null)
        .maxTemperature((forecast) ? cached.getWeatherResponse().getMaxTemperature() : null)
        .forecast((forecast) ? cached.getWeatherResponse().getForecast() : null)
        .build();

    return Optional.of(copy);
  }

  public void save(String zip, WeatherResponse response) {
    cache.put(zip,
        new CachedForecast(response, Instant.now()));
  }
}
