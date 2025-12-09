package com.hospital.api;

import com.hospital.dao.PacienteDAO;
import com.hospital.model.Paciente;
import com.hospital.util.CedulaValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

/**
 * Recurso REST para operaciones CRUD de Pacientes
 * Path base: /api/pacientes
 */
@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    /**
     * GET /api/pacientes
     * Obtener todos los pacientes
     */
    @GET
    public Response getAllPacientes() {
        try {
            List<Paciente> pacientes = pacienteDAO.findAll();
            return Response.ok(pacientes).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener pacientes: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/pacientes/{id}
     * Obtener un paciente por ID
     */
    @GET
    @Path("/{id}")
    public Response getPacienteById(@PathParam("id") Integer id) {
        try {
            Paciente paciente = pacienteDAO.findById(id);
            if (paciente != null) {
                return Response.ok(paciente).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Paciente no encontrado con ID: " + id))
                        .build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al obtener paciente: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /api/pacientes
     * Crear un nuevo paciente
     */
    @POST
    public Response createPaciente(Paciente paciente) {
        try {
            // Validar datos requeridos
            if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El nombre es requerido"))
                        .build();
            }

            if (paciente.getCedula() == null || paciente.getCedula().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("La cédula es requerida"))
                        .build();
            }

            // Validar cédula ecuatoriana con algoritmo de verificación
            String errorCedula = CedulaValidator.obtenerMensajeError(paciente.getCedula());
            if (errorCedula != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse(errorCedula))
                        .build();
            }

            // Validar que la cédula no exista
            if (pacienteDAO.cedulaExists(paciente.getCedula())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorResponse("Ya existe un paciente con esa cédula"))
                        .build();
            }

            if (paciente.getCorreo() == null || paciente.getCorreo().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El correo es requerido"))
                        .build();
            }

            if (paciente.getEdad() == null || paciente.getEdad() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("La edad debe ser mayor a 0"))
                        .build();
            }

            if (paciente.getDireccion() == null || paciente.getDireccion().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("La dirección es requerida"))
                        .build();
            }

            Paciente nuevoPaciente = pacienteDAO.create(paciente);
            return Response.status(Response.Status.CREATED)
                    .entity(nuevoPaciente)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al crear paciente: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * PUT /api/pacientes/{id}
     * Actualizar un paciente existente
     */
    @PUT
    @Path("/{id}")
    public Response updatePaciente(@PathParam("id") Integer id, Paciente paciente) {
        try {
            // Validar que el paciente exista
            Paciente existente = pacienteDAO.findById(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Paciente no encontrado con ID: " + id))
                        .build();
            }

            // Validar datos requeridos
            if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("El nombre es requerido"))
                        .build();
            }

            if (paciente.getCedula() == null || paciente.getCedula().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("La cédula es requerida"))
                        .build();
            }

            // Validar cédula ecuatoriana con algoritmo de verificación
            String errorCedula = CedulaValidator.obtenerMensajeError(paciente.getCedula());
            if (errorCedula != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse(errorCedula))
                        .build();
            }

            Paciente actualizado = pacienteDAO.update(id, paciente);
            if (actualizado != null) {
                return Response.ok(actualizado).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse("Error al actualizar paciente"))
                        .build();
            }

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al actualizar paciente: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * PUT /api/pacientes/{id}/estado
     * Activar o desactivar un paciente
     */
    @PUT
    @Path("/{id}/estado")
    public Response toggleEstadoPaciente(@PathParam("id") Integer id) {
        try {
            // Validar que el paciente exista
            Paciente existente = pacienteDAO.findById(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Paciente no encontrado con ID: " + id))
                        .build();
            }

            boolean success = pacienteDAO.toggleActivo(id);
            if (success) {
                // Obtener el paciente actualizado
                Paciente actualizado = pacienteDAO.findById(id);
                return Response.ok(actualizado).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse("Error al cambiar estado del paciente"))
                        .build();
            }

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al cambiar estado: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Clase interna para respuestas de error
     */
    public static class ErrorResponse {
        private String error;

        public ErrorResponse() {
        }

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
