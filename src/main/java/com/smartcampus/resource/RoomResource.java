/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Collection<Room> getAllRooms() {
        return DataStore.rooms.values();
    }

    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(400).entity("{\"error\":\"Room ID is required\"}").build();
        }
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(409).entity("{\"error\":\"Room already exists\"}").build();
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(404).entity("{\"error\":\"Room not found\"}").build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(404).entity("{\"error\":\"Room not found\"}").build();
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room " + roomId + " still has sensors assigned.");
        }
        DataStore.rooms.remove(roomId);
        return Response.ok("{\"message\":\"Room deleted successfully\"}").build();
    }
}