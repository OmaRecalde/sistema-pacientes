package com.hospital.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión a la base de datos PostgreSQL
 */
public class DatabaseConnection {

    // Configuración de conexión al contenedor Docker PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/hospital_db";
    private static final String USER = "hospital_user";
    private static final String PASSWORD = "hospital_pass123";

    static {
        try {
            // Cargar el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver de PostgreSQL", e);
        }
    }

    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si ocurre un error de conexión
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Cierra la conexión de manera segura
     * @param connection conexión a cerrar
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prueba la conexión a la base de datos
     * @return true si la conexión es exitosa
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
