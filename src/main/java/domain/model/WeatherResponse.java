package domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherResponse {
  private String zip;
  private Double currentTemperature;
  private Boolean fromCache;

  private Double minTemperature;
  private Double maxTemperature;
  private List<DailyForecast> forecast;
}
