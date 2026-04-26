# Smart Campus API

A RESTful API for managing university campus rooms and sensors, built with JAX-RS (Jersey) on Apache Tomcat.

## Overview
This API allows campus facilities managers to manage Rooms, Sensors, and Sensor Readings. It follows RESTful principles with proper HTTP status codes, JSON responses, and nested resource design.

## How to Build and Run

1. Clone the repository
2. Open the project in NetBeans
3. Make sure Apache Tomcat is configured
4. Right-click project → Clean and Build
5. Right-click project → Run
6. API is available at: `http://localhost:8080/SmartCampusAPI/api/v1`

## Sample curl Commands

### 1. Get all rooms
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms
```

### 2. Create a room
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"HALL-001","name":"Main Hall","capacity":200}'
```

### 3. Get all sensors filtered by type
```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2"
```

### 4. Add a sensor reading
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"value":25.5}'
```

### 5. Delete a room
```bash
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/HALL-001
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1 | Discovery endpoint |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a room |
| GET | /api/v1/rooms/{id} | Get a specific room |
| DELETE | /api/v1/rooms/{id} | Delete a room |
| GET | /api/v1/sensors | Get all sensors |
| POST | /api/v1/sensors | Create a sensor |
| GET | /api/v1/sensors/{id} | Get a specific sensor |
| GET | /api/v1/sensors/{id}/readings | Get readings for a sensor |
| POST | /api/v1/sensors/{id}/readings | Add a reading |
