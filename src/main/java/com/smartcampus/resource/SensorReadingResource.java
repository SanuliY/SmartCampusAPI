/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        if (!DataStore.sensors.containsKey(sensorId)) {
            return Response.status(404).entity("{\"error\":\"Sensor not found\"}").build();
        }
        List<SensorReading> list = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(list).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(404).entity("{\"error\":\"Sensor not found\"}").build();
        }
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is under maintenance.");
        }
        SensorReading newReading = new SensorReading(reading.getValue());
        DataStore.readings.get(sensorId).add(newReading);
        sensor.setCurrentValue(reading.getValue());
        return Response.status(201).entity(newReading).build();
    }
}