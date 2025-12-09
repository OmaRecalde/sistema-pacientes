package com.hospital.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Recurso REST de prueba para verificar que el servidor funciona
 */
@Path("/hello")
public class HelloResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello() {
        return Response.ok()
                .entity("{\"message\": \"Sistema de Pacientes API funcionando correctamente\"}")
                .build();
    }
}
