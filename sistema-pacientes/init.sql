-- Script de inicialización de base de datos para Sistema de Pacientes
-- Base de datos: hospital_db

-- Crear tabla pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL,
    edad INTEGER NOT NULL CHECK (edad > 0 AND edad < 150),
    direccion VARCHAR(200) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de prueba
INSERT INTO pacientes (nombre, cedula, correo, edad, direccion, activo) VALUES
('Juan Pérez García', '1234567890', 'juan.perez@email.com', 35, 'Av. América N23-45 y Mariana de Jesús', true),
('María Rodríguez López', '0987654321', 'maria.rodriguez@email.com', 28, 'Calle García Moreno 567', true),
('Carlos Sánchez Mora', '1726354890', 'carlos.sanchez@email.com', 42, 'Av. 6 de Diciembre N34-67', true),
('Ana Torres Vega', '1712345678', 'ana.torres@email.com', 31, 'Calle Guayaquil y Olmedo', true),
('Luis Morales Cruz', '1798765432', 'luis.morales@email.com', 55, 'Av. Naciones Unidas E4-123', false);

-- Verificar datos insertados
SELECT * FROM pacientes;
