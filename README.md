# Weather Forecast API

A Spring Boot REST API that retrieves weather information for a given ZIP code.  
The API provides the **current temperature** as the primary response and optionally returns **daily min/max temperatures and an extended forecast** as bonus features.

The project was designed with clean architecture principles, clear separation of responsibilities, and a cache-first approach to minimize external API calls.

---

## Features

- Retrieve current temperature by ZIP code
- Optional extended forecast (daily min/max temperatures)
- In-memory cache with 15-minute TTL
- Cache indicator (`fromCache`) in the response
- Graceful error handling
- No API keys required (100% open-source APIs)

---

## Tech Stack & Versions

| Tool / Framework       | Version               |
|------------------------|-----------------------|
| Java                   | 21                     |
| Spring Boot            | 3.x                   |
| Spring Web (REST)      | Included              |
| Jackson                | Default (Spring Boot) |
| Maven                  | 3.9+                  |
| JUnit / Mockito        | 5.x                   |
| HTTP Client            | RestTemplate          |

---

## External APIs Used

### Geocoding (ZIP → Latitude/Longitude)
- **Nominatim (OpenStreetMap)**
- No API key required
- Used to resolve ZIP codes into geographic coordinates

### Weather Forecast
- **Open-Meteo API**
- No API key required
- Provides current weather and daily forecasts

Both services are rate-limited and therefore protected by an internal cache.

---

## Architecture Overview

The application follows standard Spring Boot layering:

### Key Design Principles

- Separation of concerns (Controller, Service, Model)
- DTO isolation (external API DTOs never exposed to clients)
- Cache integrity (cached objects are never mutated)
- Progressive response enrichment (same endpoint, optional data)

---

## Cache Strategy

- In-memory cache using `ConcurrentHashMap`
- Cache key: ZIP code
- TTL: **15 minutes**
- Cache always stores the **full weather response**
- Any variation in the API payload (e.g., excluding forecast) is applied on a **defensive copy**, preventing shared mutable state

This design allows easy replacement with Redis or Caffeine in the future.

---

## API Endpoint

### GET `/weather`

#### Query Parameters

| Name | Required | Description |
|----|---------|------------|
| `zip` | Yes | ZIP code |
| `forecast` | No | Include extended forecast (`true` / `false`, default: `false`) |

---

### Example – Without Forecast

```http
GET /api/weather?zip=90210
```

```json
{
  "zip": "90210",
  "currentTemperature": 23.9,
  "fromCache": false
}
```

### Example – With Forecast

```http
GET /api/weather?zip=90210&forecast=true
```

```json
{
  "zip": "90210",
  "currentTemperature": 23.9,
  "minTemperature": 15.5,
  "maxTemperature": 27.3,
  "forecast": [
    { "date": "2026-01-30", "min": 15.5, "max": 27.3 },
    { "date": "2026-01-31", "min": 17.6, "max": 28.7 }
  ],
  "fromCache": false
}
```

| Scenario                 | HTTP Status           |
| ------------------------ | --------------------- |
| Invalid ZIP format       | `400 Bad Request`     |
| ZIP not found            | `404 Not Found`       |
| External API unavailable | `502 Bad Gateway`     |
| External API timeout     | `504 Gateway Timeout` |

```json
{
  "status": 404,
  "error": "ZIP_CODE_NOT_FOUND",
  "message": "The provided zip code could not be resolved to a location"
}
```

## How to Run the Project
### Prerequisites

* Java 21+
* Maven 3.9+

### Steps
```git
git clone https://github.com/your-username/weather-api.git
cd weather-api

export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH=$JAVA_HOME/bin:$PATH

mvn clean install spring-boot:run
```

The application will start on:

http://localhost:8080

## Running Tests
```maven
mvn test
```

## Architectural Decisions

### Why a single endpoint?
The API uses a single /weather endpoint with optional parameters to avoid endpoint proliferation and keep the contract simple and evolvable.

### Why not HATEOAS?
HATEOAS would add unnecessary complexity for a single-purpose API without navigable resources.

### Why RestTemplate?
Chosen for simplicity and clarity. The design allows easy migration to WebClient if reactive support becomes necessary.

## Future Improvements

Replace in-memory cache with Redis

Add metrics and observability

Add API versioning

Support international ZIP/postal code formats