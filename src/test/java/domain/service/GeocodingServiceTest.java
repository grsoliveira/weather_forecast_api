package domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.exception.DataNotFoundException;
import domain.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class GeocodingServiceTest {

  private RestTemplate restTemplate;
  private GeocodingService geocodingService;
  private final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    restTemplate = mock(RestTemplate.class);
    geocodingService = new GeocodingService();
    ReflectionTestUtils.setField(geocodingService, "restTemplate", restTemplate);
  }

  @Test
  void testGetLocationByZipSuccess() throws Exception {
    String zip = "90211";

    String json = """
        {
          "country": "United States",
          "country abbreviation": "US",
          "post code": "90211",
          "places": [
            {
              "place name": "Beverly Hills",
              "longitude": "-118.4108",
              "latitude": "34.1030",
              "state": "California",
              "state abbreviation": "CA"
            }
          ]
        }
        """;

    JsonNode node = mapper.readTree(json);
    when(restTemplate.getForObject(eq("http://api.zippopotam.us/us/" + zip), eq(JsonNode.class)))
        .thenReturn(node);

    Optional<Location> result = geocodingService.getLocationByZip(zip);

    assertTrue(result.isPresent());
    Location loc = result.get();
    assertEquals(zip, loc.getZip());
    assertEquals(34.1030, loc.getLatitude());
    assertEquals(-118.4108, loc.getLongitude());
  }

  @Test
  void testGetLocationByZipThrowsDataNotFoundWhenHttpClientError() {
    when(restTemplate.getForObject(anyString(), eq(JsonNode.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    assertThrows(DataNotFoundException.class, () -> geocodingService.getLocationByZip("00000"));

    verify(restTemplate, times(1)).getForObject(anyString(), eq(JsonNode.class));
  }

}
