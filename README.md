# Toll Plazas Between Two Pincodes - Spring Boot Backend

## Overview
This Spring Boot application provides a REST API to determine toll plazas located between two Indian pincodes. It simulates a route using the Google Directions API and returns details about toll plazas along the way, using a provided CSV file for toll plaza data.

## Features
- Accepts source and destination pincodes as input
- Returns a list of toll plazas on the route, including:
  - Toll plaza name
  - Latitude and longitude
  - Distance from the source pincode
- Uses caching for repeated requests
- Handles edge cases (invalid pincodes, no tolls, same source/destination)
- Robust error handling and validation
- Modular, testable, and REST-compliant

## API Documentation
### Endpoint
```
POST /api/v1/toll-plazas
```

### Request Body
```
{
  "sourcePincode": "110001",
  "destinationPincode": "560001"
}
```

### Success Response
```
{
  "route": {
    "sourcePincode": "110001",
    "destinationPincode": "560001",
    "distanceInKm": 2100
  },
  "tollPlazas": [
    {
      "name": "Toll Plaza 1",
      "latitude": 28.7041,
      "longitude": 77.1025,
      "distanceFromSource": 200
    },
    {
      "name": "Toll Plaza 2",
      "latitude": 19.076,
      "longitude": 72.8777,
      "distanceFromSource": 1400
    }
  ]
}
```

### Error Responses
- **Invalid Pincode:**
  ```
  {
    "error": "Invalid source or destination pincode",
    "route": {
      "sourcePincode": "110001",
      "destinationPincode": "560001",
      "distanceInKm": 2100
    },
    "tollPlazas": []
  }
  ```
- **No Toll Plazas:**
  ```
  {
    "route": { ... },
    "tollPlazas": []
  }
  ```
- **Same Source and Destination:**
  ```
  {
    "error": "Source and destination pincodes cannot be the same"
  }
  ```

## Setup & Running
1. **Clone the repository:**
   ```
   git clone <repo-url>
   cd assignmenttoll/tollservice
   ```
2. **Configure API Keys:**
   - Set your Google Directions API key in `src/main/resources/application.properties`:
     ```
     google.api.key=YOUR_GOOGLE_DIRECTIONS_API_KEY
     ```
   - Ensure your `toll_plazas.csv` is in `src/main/resources/`.
3. **Build and run:**
   ```
   ./mvnw spring-boot:run
   ```
4. **Test the API:**
   Use Postman or curl:
   ```
   curl -X POST http://localhost:8080/api/v1/toll-plazas \
     -H "Content-Type: application/json" \
     -d '{"sourcePincode": "400001", "destinationPincode": "560001"}'
   ```
## Notes
- The route is simulated using Google Directions API.
- Toll plaza data is loaded from the provided CSV file.

## License
MIT 
