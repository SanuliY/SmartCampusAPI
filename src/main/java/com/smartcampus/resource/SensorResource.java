/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        Collection<Sensor> all = DataStore.sensors.values();
        if (type != null && !type.isEmpty()) {
            List<Sensor> filtered = all.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
            return Response.ok(filtered).build();
        }
        return Response.ok(new ArrayList<>(all)).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getRoomId() == null || !DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room with ID '" + sensor.getRoomId() + "' does not exist.");
        }
        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(409).entity("{\"error\":\"Sensor already exists\"}").build();
        }
        DataStore.sensors.put(sensor.getId(), sensor);
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        DataStore.readings.put(sensor.getId(), new ArrayList<>());
        return Response.status(201).entity(sensor).build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(404).entity("{\"error\":\"Sensor not found\"}").build();
        }
        return Response.ok(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}