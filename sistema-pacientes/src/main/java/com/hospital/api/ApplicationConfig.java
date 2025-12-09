package com.hospital.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Clase de configuración JAX-RS
 * Define el path base para todos los recursos REST
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    // No requiere implementación adicional
    // JAX-RS automáticamente descubre los recursos REST
}
