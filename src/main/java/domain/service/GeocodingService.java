package domain.service;

import domain.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

  private final RestTemplate restTemplate = new RestTemplate();

  public Location getLocationByZip(String zip) {
    String url = "http://api.zippopotam.us/us/" + zip;
    return restTemplate.getForObject(url, Location.class);
  }

}
