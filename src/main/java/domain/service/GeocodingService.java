package domain.service;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import domain.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

  private final RestTemplate restTemplate = new RestTemplate();

  public Optional<Location> getLocationByZip(String zip) {

    //result from http://api.zippopotam.us/us/90210
    //{"country": "United States", "country abbreviation": "US", "post code": "90210", "places": [{"place name": "Beverly Hills", "longitude": "-118.4065", "latitude": "34.0901", "state": "California", "state abbreviation": "CA"}]}

    final String url = "http://api.zippopotam.us/us/" + zip;
    JsonNode node = restTemplate.getForObject(url, JsonNode.class);

    if (node == null) {
      return Optional.empty();
    }

    JsonNode placeNode = node.get("places").get(0);
    if (placeNode == null) {
      return Optional.empty();
    }

    String latitude = placeNode.get("latitude").asText();
    String longitude = placeNode.get("longitude").asText();

    if (latitude == null || longitude == null) {
      return Optional.empty();
    }

    return Optional.of(new Location(Double.parseDouble(latitude), Double.parseDouble(longitude)));
  }

}
