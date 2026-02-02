package domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.DailyForecast;
import domain.model.Location;
import domain.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class OpenMeteoServiceTest {

  private RestTemplate restTemplate;
  private OpenMeteoService openMeteoService;
  private final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    restTemplate = mock(RestTemplate.class);
    openMeteoService = new OpenMeteoService();

    // Injeta o RestTemplate mockado
    ReflectionTestUtils.setField(openMeteoService, "restTemplate", restTemplate);
  }

  @Test
  void testGetWeatherByLocationSuccess() throws Exception {

    Location location = new Location();
    location.setZip("90211");
    location.setLatitude(34.1030);
    location.setLongitude(-118.4108);

    String json = """
        {
          "current_weather": {
            "temperature": 22.5
          },
          "daily": {
            "time": [
              "2026-02-01",
              "2026-02-02"
            ],
            "temperature_2m_min": [15.0, 16.0],
            "temperature_2m_max": [25.0, 26.0]
          }
        }
        """;

    JsonNode node = mapper.readTree(json);

    when(restTemplate.getForObject(anyString(), eq(JsonNode.class)))
        .thenReturn(node);

    Optional<WeatherResponse> result =
        openMeteoService.getWeatherByLocation(location);

    assertTrue(result.isPresent());

    WeatherResponse response = result.get();

    assertEquals("90211", response.getZip());
    assertEquals(22.5, response.getCurrentTemperature());
    assertEquals(25.0, response.getMaxTemperature());
    assertEquals(15.0, response.getMinTemperature());

    assertEquals(2, response.getForecast().size());

    DailyForecast firstDay = response.getForecast().get(0);
    assertEquals("2026-02-01", firstDay.getDate());
    assertEquals(15.0, firstDay.getMin());
    assertEquals(25.0, firstDay.getMax());
  }

  @Test
  void testGetWeatherByLocationReturnsEmptyWhenCurrentWeatherMissing() throws Exception {

    Location location = new Location();
    location.setZip("90211");
    location.setLatitude(34.1030);
    location.setLongitude(-118.4108);

    String json = """
        {
          "daily": {
            "time": ["2026-02-01"],
            "temperature_2m_min": [15.0],
            "temperature_2m_max": [25.0]
          }
        }
        """;

    JsonNode node = mapper.readTree(json);

    when(restTemplate.getForObject(anyString(), eq(JsonNode.class)))
        .thenReturn(node);

    Optional<WeatherResponse> result =
        openMeteoService.getWeatherByLocation(location);

    assertTrue(result.isEmpty());
  }
}
