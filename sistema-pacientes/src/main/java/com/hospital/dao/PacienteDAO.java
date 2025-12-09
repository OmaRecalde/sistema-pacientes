package com.hospital.dao;

import com.hospital.model.Paciente;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para operaciones CRUD de Paciente
 */
public class PacienteDAO {

    /**
     * Obtener todos los pacientes
     */
    public List<Paciente> findAll() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(rs));
            }
        }
        return pacientes;
    }

    /**
     * Obtener un paciente por ID
     */
    public Paciente findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        Paciente paciente = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paciente = mapResultSetToPaciente(rs);
                }
            }
        }
        return paciente;
    }

    /**
     * Crear un nuevo paciente
     */
    public Paciente create(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO pacientes (nombre, cedula, correo, edad, direccion, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id, fecha_registro";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paciente.getNombre());
            pstmt.setString(2, paciente.getCedula());
            pstmt.setString(3, paciente.getCorreo());
            pstmt.setInt(4, paciente.getEdad());
            pstmt.setString(5, paciente.getDireccion());
            pstmt.setBoolean(6, paciente.getActivo() != null ? paciente.getActivo() : true);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paciente.setId(rs.getInt("id"));
                    paciente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                }
            }
        }
        return paciente;
    }

    /**
     * Actualizar un paciente existente
     */
    public Paciente update(Integer id, Paciente paciente) throws SQLException {
        String sql = "UPDATE pacientes SET nombre = ?, cedula = ?, correo = ?, " +
                     "edad = ?, direccion = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paciente.getNombre());
            pstmt.setString(2, paciente.getCedula());
            pstmt.setString(3, paciente.getCorreo());
            pstmt.setInt(4, paciente.getEdad());
            pstmt.setString(5, paciente.getDireccion());
            pstmt.setInt(6, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                paciente.setId(id);
                return findById(id);
            }
        }
        return null;
    }

    /**
     * Activar o desactivar un paciente
     */
    public boolean toggleActivo(Integer id) throws SQLException {
        String sql = "UPDATE pacientes SET activo = NOT activo WHERE id = ? RETURNING activo";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Eliminar un paciente
     */
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM pacientes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Verificar si una cédula ya existe
     */
    public boolean cedulaExists(String cedula) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pacientes WHERE cedula = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Mapear ResultSet a objeto Paciente
     */
    private Paciente mapResultSetToPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("id"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setCedula(rs.getString("cedula"));
        paciente.setCorreo(rs.getString("correo"));
        paciente.setEdad(rs.getInt("edad"));
        paciente.setDireccion(rs.getString("direccion"));
        paciente.setActivo(rs.getBoolean("activo"));
        paciente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return paciente;
    }
}
